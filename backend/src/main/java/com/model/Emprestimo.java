
package com.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Long id;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(precision = 10, scale = 2)
    private BigDecimal multa = BigDecimal.ZERO;

    private Boolean atraso = false;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula", nullable = false)
    private Aluno aluno;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEmprestimo> itens = new ArrayList<>();

    public Emprestimo() {
    }

    public Emprestimo(LocalDate dataEmprestimo, LocalDate dataPrevista, Aluno aluno) {
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.aluno = aluno;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public Boolean getAtraso() {
        return atraso;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public List<ItemEmprestimo> getItens() {
        return itens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public void setAtraso(Boolean atraso) {
        this.atraso = atraso;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void setItens(List<ItemEmprestimo> itens) {
        this.itens = itens;
    }
}