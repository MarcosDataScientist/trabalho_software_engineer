package com.biblioteca.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DevolucaoResponse(
        Long idEmprestimo,
        LocalDate dataDevolucao,
        Boolean atraso,
        BigDecimal multaGerada,
        BigDecimal multaTotalEmprestimo,
        List<ItemDevolvidoResponse> livrosDevolvidos
) {
}
