package com.biblioteca.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutorRequest(
        @NotBlank(message = "O nome do autor é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 100, message = "O sobrenome deve ter no máximo 100 caracteres")
        String sobrenome,

        @Size(max = 100, message = "A titulação deve ter no máximo 100 caracteres")
        String titulacao
) {
}
