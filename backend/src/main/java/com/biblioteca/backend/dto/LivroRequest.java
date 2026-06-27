package com.biblioteca.backend.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record LivroRequest(
        Long tituloId,

        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @Positive(message = "O prazo deve ser maior que zero")
        Integer prazo,

        @Size(max = 20, message = "O ISBN deve ter no máximo 20 caracteres")
        String isbn,

        Integer edicao,

        Integer ano,

        Long areaId,

        List<Long> autorIds,

        Boolean disponivel,

        Boolean exemplarBiblioteca
) {
}
