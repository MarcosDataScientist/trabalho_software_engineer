package com.biblioteca.backend.dto;

import java.util.List;

public record LivroResponse(
        Long id,
        Boolean disponivel,
        Boolean exemplarBiblioteca,
        Long tituloId,
        String titulo,
        Integer prazo,
        String isbn,
        Integer edicao,
        Integer ano,
        Long areaId,
        String areaNome,
        List<AutorResponse> autores
) {
}
