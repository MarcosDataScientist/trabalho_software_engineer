package com.biblioteca.backend.service;

import com.biblioteca.backend.config.MultaProperties;
import com.biblioteca.backend.dto.DevolucaoRequest;
import com.biblioteca.backend.dto.DevolucaoResponse;
import com.biblioteca.backend.dto.EmprestimoRequest;
import com.biblioteca.backend.dto.EmprestimoResponse;
import com.biblioteca.backend.dto.ItemDevolvidoResponse;
import com.biblioteca.backend.dto.ItemEmprestimoResponse;
import com.biblioteca.backend.exception.BusinessRuleException;
import com.biblioteca.backend.exception.ResourceNotFoundException;
import com.biblioteca.backend.model.Aluno;
import com.biblioteca.backend.model.Debito;
import com.biblioteca.backend.model.Emprestimo;
import com.biblioteca.backend.model.ItemEmprestimo;
import com.biblioteca.backend.model.Livro;
import com.biblioteca.backend.repository.AlunoRepository;
import com.biblioteca.backend.repository.DebitoRepository;
import com.biblioteca.backend.repository.EmprestimoRepository;
import com.biblioteca.backend.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final AlunoRepository alunoRepository;
    private final LivroRepository livroRepository;
    private final DebitoRepository debitoRepository;
    private final MultaProperties multaProperties;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            AlunoRepository alunoRepository,
            LivroRepository livroRepository,
            DebitoRepository debitoRepository,
            MultaProperties multaProperties
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.alunoRepository = alunoRepository;
        this.livroRepository = livroRepository;
        this.debitoRepository = debitoRepository;
        this.multaProperties = multaProperties;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public EmprestimoResponse emprestar(EmprestimoRequest request) {
        Aluno aluno = alunoRepository.findByMatriculaWithDebitos(request.matricula())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não está cadastrado"));

        if (aluno.possuiPendenciasFinanceiras()) {
            throw new BusinessRuleException("Aluno possui pendências financeiras");
        }

        Emprestimo emprestimo = Emprestimo.novo(aluno);

        for (Long livroId : request.livrosIds()) {
            Livro livro = livroRepository.findByIdParaEmprestimo(livroId)
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com id " + livroId));

            if (!livro.podeSerEmprestado()) {
                throw new BusinessRuleException(livro.getMotivoImpedimentoEmprestimo());
            }

            emprestimo.adicionarLivro(livro);
        }

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return toResponse(salvo);
    }

    @Transactional
    public DevolucaoResponse devolver(DevolucaoRequest request) {
        validarIdentificacaoDevolucao(request);

        Emprestimo emprestimo = localizarEmprestimo(request);
        validarItensDevolucao(emprestimo, request.livrosIds());

        LocalDate dataDevolucao = LocalDate.now();
        BigDecimal taxaDiaria = multaProperties.getValorPorDia();

        Emprestimo.ResultadoDevolucao resultado = emprestimo.registrarDevolucaoDeItens(
                request.livrosIds(),
                dataDevolucao,
                taxaDiaria
        );

        if (resultado.multaGerada().compareTo(BigDecimal.ZERO) > 0) {
            registrarDebito(emprestimo.getAluno(), resultado.multaGerada(), dataDevolucao);
        }

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return toDevolucaoResponse(salvo, request.livrosIds(), dataDevolucao, taxaDiaria, resultado);
    }

    private void validarIdentificacaoDevolucao(DevolucaoRequest request) {
        if (request.emprestimoId() == null && request.matricula() == null) {
            throw new BusinessRuleException("Informe o id do empréstimo ou a matrícula do aluno");
        }
    }

    private Emprestimo localizarEmprestimo(DevolucaoRequest request) {
        if (request.emprestimoId() != null) {
            return emprestimoRepository.findByIdParaDevolucao(request.emprestimoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Empréstimo não localizado, verifique as informações fornecidas!"));
        }

        List<Emprestimo> emprestimosAtivos = emprestimoRepository.findEmprestimosAtivosByMatricula(request.matricula());
        return emprestimosAtivos.stream()
                .filter(emprestimo -> contemLivrosParaDevolucao(emprestimo, request.livrosIds()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empréstimo não localizado, verifique as informações fornecidas!"));
    }

    private boolean contemLivrosParaDevolucao(Emprestimo emprestimo, List<Long> livrosIds) {
        return livrosIds.stream()
                .allMatch(livroId -> emprestimo.buscarItemPorLivro(livroId)
                        .filter(item -> !item.foiDevolvido())
                        .isPresent());
    }

    private void validarItensDevolucao(Emprestimo emprestimo, List<Long> livrosIds) {
        if (!emprestimo.possuiItensPassiveisDeDevolucao()) {
            throw new BusinessRuleException("A devolucão não é possível, verifique as informações fornecidas!");
        }

        for (Long livroId : livrosIds) {
            ItemEmprestimo item = emprestimo.buscarItemPorLivro(livroId)
                    .orElseThrow(() -> new BusinessRuleException(
                            "Existem livros nao associados ao emprestimo. Verifique a lista de livros!"));

            if (item.foiDevolvido()) {
                throw new BusinessRuleException(
                        "Existem livros já devolvidos anteriormente, favor verificar as informações.");
            }
        }
    }

    private void registrarDebito(Aluno aluno, BigDecimal valor, LocalDate data) {
        try {
            debitoRepository.save(new Debito(valor, data, aluno));
        } catch (RuntimeException ex) {
            throw new BusinessRuleException("Erro ao registrar débito, devolução cancelada...");
        }
    }

    private DevolucaoResponse toDevolucaoResponse(
            Emprestimo emprestimo,
            List<Long> livrosIds,
            LocalDate dataDevolucao,
            BigDecimal taxaDiaria,
            Emprestimo.ResultadoDevolucao resultado
    ) {
        List<ItemDevolvidoResponse> itensDevolvidos = new ArrayList<>();
        for (Long livroId : livrosIds) {
            ItemEmprestimo item = emprestimo.buscarItemPorLivro(livroId).orElseThrow();
            itensDevolvidos.add(new ItemDevolvidoResponse(
                    item.getLivro().getId(),
                    item.getLivro().getTitulo().getTitulo(),
                    item.getDataPrevista(),
                    item.getDataDevolucao(),
                    item.calcularMulta(dataDevolucao, taxaDiaria)
            ));
        }

        return new DevolucaoResponse(
                emprestimo.getId(),
                dataDevolucao,
                resultado.houveAtraso(),
                resultado.multaGerada(),
                emprestimo.getMulta(),
                itensDevolvidos
        );
    }

    private EmprestimoResponse toResponse(Emprestimo emprestimo) {
        List<ItemEmprestimoResponse> itens = emprestimo.getItens().stream()
                .map(this::toItemResponse)
                .toList();

        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevista(),
                emprestimo.getDataDevolucao(),
                emprestimo.getMulta(),
                emprestimo.getValor(),
                emprestimo.getAtraso(),
                emprestimo.getAluno().getMatricula(),
                emprestimo.getAluno().getNome(),
                itens
        );
    }

    private ItemEmprestimoResponse toItemResponse(ItemEmprestimo item) {
        return new ItemEmprestimoResponse(
                item.getLivro().getId(),
                item.getLivro().getTitulo().getTitulo(),
                item.getDataPrevista()
        );
    }
}
