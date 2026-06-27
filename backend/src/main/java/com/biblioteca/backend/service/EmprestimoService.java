package com.biblioteca.backend.service;

import com.biblioteca.backend.dto.EmprestimoRequest;
import com.biblioteca.backend.dto.EmprestimoResponse;
import com.biblioteca.backend.dto.ItemEmprestimoResponse;
import com.biblioteca.backend.exception.BusinessRuleException;
import com.biblioteca.backend.exception.ResourceNotFoundException;
import com.biblioteca.backend.repository.AlunoRepository;
import com.biblioteca.backend.repository.EmprestimoRepository;
import com.biblioteca.backend.repository.LivroRepository;
import com.model.Aluno;
import com.model.Emprestimo;
import com.model.ItemEmprestimo;
import com.model.Livro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final AlunoRepository alunoRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            AlunoRepository alunoRepository,
            LivroRepository livroRepository
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.alunoRepository = alunoRepository;
        this.livroRepository = livroRepository;
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
