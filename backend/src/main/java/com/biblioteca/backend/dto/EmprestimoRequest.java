package com.biblioteca.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EmprestimoRequest(
        @NotNull(message = "A matrícula do aluno é obrigatória")
        Long matricula,

        @NotEmpty(message = "Informe ao menos um livro para empréstimo")
        List<@NotNull(message = "O identificador do livro não pode ser nulo") Long> livrosIds
) {
}
