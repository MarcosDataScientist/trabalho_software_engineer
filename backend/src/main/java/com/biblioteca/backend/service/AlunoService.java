package com.biblioteca.backend.service;

import com.biblioteca.backend.dto.AlunoRequest;
import com.biblioteca.backend.dto.AlunoResponse;
import com.biblioteca.backend.exception.BusinessRuleException;
import com.biblioteca.backend.exception.ResourceNotFoundException;
import com.biblioteca.backend.repository.AlunoRepository;
import com.biblioteca.backend.model.Aluno;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorMatricula(Long matricula) {
        return toResponse(buscarEntidade(matricula));
    }

    public AlunoResponse cadastrar(AlunoRequest request) {
        if (alunoRepository.existsById(request.matricula())) {
            throw new BusinessRuleException("Já existe um aluno com a matrícula informada");
        }
        if (alunoRepository.existsByCpf(request.cpf())) {
            throw new BusinessRuleException("Já existe um aluno com o CPF informado");
        }

        Aluno aluno = new Aluno(
                request.matricula(),
                request.nome(),
                request.cpf(),
                request.endereco()
        );
        return toResponse(alunoRepository.save(aluno));
    }

    public AlunoResponse atualizar(Long matricula, AlunoRequest request) {
        if (!matricula.equals(request.matricula())) {
            throw new BusinessRuleException("A matrícula informada no corpo da requisição deve ser igual à da URL");
        }

        Aluno aluno = buscarEntidade(matricula);

        alunoRepository.findByCpf(request.cpf())
                .filter(existing -> !existing.getMatricula().equals(matricula))
                .ifPresent(existing -> {
                    throw new BusinessRuleException("Já existe um aluno com o CPF informado");
                });

        aluno.setNome(request.nome());
        aluno.setCpf(request.cpf());
        aluno.setEndereco(request.endereco());

        return toResponse(alunoRepository.save(aluno));
    }

    public void excluir(Long matricula) {
        Aluno aluno = buscarEntidade(matricula);
        alunoRepository.delete(aluno);
    }

    @Transactional(readOnly = true)
    public Aluno buscarEntidade(Long matricula) {
        return alunoRepository.findById(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com matrícula " + matricula));
    }

    private AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getEndereco()
        );
    }
}
