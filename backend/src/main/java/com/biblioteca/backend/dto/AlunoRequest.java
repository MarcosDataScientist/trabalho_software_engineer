package com.biblioteca.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlunoRequest(
        @NotNull(message = "A matrícula é obrigatória")
        Long matricula,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres")
        String cpf,

        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
        String endereco
) {
}
