package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.AreaRequest;
import com.biblioteca.backend.dto.AreaResponse;
import com.biblioteca.backend.dto.AutorRequest;
import com.biblioteca.backend.dto.AutorResponse;
import com.biblioteca.backend.dto.LivroRequest;
import com.biblioteca.backend.dto.LivroResponse;
import com.biblioteca.backend.service.LivroService;
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
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public List<LivroResponse> listar() {
        return livroService.listarTodos();
    }

    @GetMapping("/disponiveis")
    public List<LivroResponse> listarDisponiveis() {
        return livroService.listarDisponiveis();
    }

    @GetMapping("/catalogo/areas")
    public List<AreaResponse> listarAreas() {
        return livroService.listarAreas();
    }

    @PostMapping("/catalogo/areas")
    @ResponseStatus(HttpStatus.CREATED)
    public AreaResponse cadastrarArea(@Valid @RequestBody AreaRequest request) {
        return livroService.cadastrarArea(request);
    }

    @GetMapping("/catalogo/autores")
    public List<AutorResponse> listarAutores() {
        return livroService.listarAutores();
    }

    @PostMapping("/catalogo/autores")
    @ResponseStatus(HttpStatus.CREATED)
    public AutorResponse cadastrarAutor(@Valid @RequestBody AutorRequest request) {
        return livroService.cadastrarAutor(request);
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponse cadastrar(@Valid @RequestBody LivroRequest request) {
        return livroService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequest request
    ) {
        return livroService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }
}
