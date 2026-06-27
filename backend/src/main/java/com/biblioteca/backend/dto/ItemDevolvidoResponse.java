package com.biblioteca.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ItemDevolvidoResponse(
        Long idLivro,
        String tituloLivro,
        LocalDate dataPrevista,
        LocalDate dataDevolucao,
        BigDecimal multa
) {
}
