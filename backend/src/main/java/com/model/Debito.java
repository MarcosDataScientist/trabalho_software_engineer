
package com.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "debito")
public class Debito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_debito")
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula", nullable = false)
    private Aluno aluno;

    public Debito() {
    }

    public Debito(BigDecimal valor, LocalDate data, Aluno aluno) {
        this.valor = valor;
        this.data = data;
        this.aluno = aluno;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}