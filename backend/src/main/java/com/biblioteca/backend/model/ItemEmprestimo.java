
package com.biblioteca.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "item_emprestimo")
public class ItemEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_emprestimo")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_emprestimo", nullable = false)
    private Emprestimo emprestimo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    public ItemEmprestimo() {
    }

    public ItemEmprestimo(Emprestimo emprestimo, Livro livro, LocalDate dataPrevista) {
        this.emprestimo = emprestimo;
        this.livro = livro;
        this.dataPrevista = dataPrevista;
    }

    public Long getId() {
        return id;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean foiDevolvido() {
        return dataDevolucao != null;
    }

    public long calcularDiasAtraso(LocalDate dataDevolucao) {
        if (dataDevolucao.isAfter(dataPrevista)) {
            return ChronoUnit.DAYS.between(dataPrevista, dataDevolucao);
        }
        return 0;
    }

    public BigDecimal calcularMulta(LocalDate dataDevolucao, BigDecimal taxaDiaria) {
        long diasAtraso = calcularDiasAtraso(dataDevolucao);
        if (diasAtraso <= 0) {
            return BigDecimal.ZERO;
        }
        return taxaDiaria.multiply(BigDecimal.valueOf(diasAtraso));
    }

    public BigDecimal devolver(LocalDate dataDevolucao, BigDecimal taxaDiaria) {
        this.dataDevolucao = dataDevolucao;
        livro.marcarComoDisponivel();
        return calcularMulta(dataDevolucao, taxaDiaria);
    }
}