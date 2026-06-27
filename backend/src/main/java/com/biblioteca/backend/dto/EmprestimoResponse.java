package com.biblioteca.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record EmprestimoResponse(
        Long id,
        LocalDate dataEmprestimo,
        LocalDate dataPrevista,
        LocalDate dataDevolucao,
        BigDecimal multa,
        BigDecimal valor,
        Boolean atraso,
        Long matriculaAluno,
        String nomeAluno,
        List<ItemEmprestimoResponse> livrosEmprestados
) {
}
