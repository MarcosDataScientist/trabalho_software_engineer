package com.biblioteca.backend.dto;

import java.time.LocalDate;

public record ItemEmprestimoResponse(
        Long idLivro,
        String tituloLivro,
        LocalDate dataPrevista
) {
}
