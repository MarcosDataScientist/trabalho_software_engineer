package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.AlunoRequest;
import com.biblioteca.backend.dto.AlunoResponse;
import com.biblioteca.backend.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public List<AlunoResponse> listar() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{matricula}")
    public AlunoResponse buscar(@PathVariable Long matricula) {
        return alunoService.buscarPorMatricula(matricula);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponse cadastrar(@Valid @RequestBody AlunoRequest request) {
        return alunoService.cadastrar(request);
    }

    @PutMapping("/{matricula}")
    public AlunoResponse atualizar(
            @PathVariable Long matricula,
            @Valid @RequestBody AlunoRequest request
    ) {
        return alunoService.atualizar(matricula, request);
    }

    @DeleteMapping("/{matricula}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long matricula) {
        alunoService.excluir(matricula);
    }
}
