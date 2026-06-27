package com.biblioteca.backend.service;

import com.biblioteca.backend.dto.EmprestimoResponse;
import com.biblioteca.backend.repository.EmprestimoRepository;
import com.model.Emprestimo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }

    public List<EmprestimoResponse> listarTodos() {
        return emprestimoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private EmprestimoResponse toResponse(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevista(),
                emprestimo.getDataDevolucao(),
                emprestimo.getMulta(),
                emprestimo.getValor(),
                emprestimo.getAtraso(),
                emprestimo.getAluno().getMatricula(),
                emprestimo.getAluno().getNome()
        );
    }
}
