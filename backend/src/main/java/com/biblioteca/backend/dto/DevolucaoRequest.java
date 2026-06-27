package com.biblioteca.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DevolucaoRequest(
        Long emprestimoId,
        Long matricula,
        @NotEmpty(message = "Informe ao menos um livro para devolução")
        List<@NotNull(message = "O identificador do livro não pode ser nulo") Long> livrosIds
) {
}
