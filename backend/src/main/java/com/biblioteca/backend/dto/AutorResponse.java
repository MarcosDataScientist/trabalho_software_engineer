package com.biblioteca.backend.dto;

public record AutorResponse(
        Long id,
        String nome,
        String sobrenome,
        String titulacao
) {
}
