package com.biblioteca.backend.dto;

public record AlunoResponse(
        Long matricula,
        String nome,
        String cpf,
        String endereco
) {
}
