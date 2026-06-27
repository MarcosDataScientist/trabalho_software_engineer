package com.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula", nullable = false)
    private Aluno aluno;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaLivro> livros = new ArrayList<>();

    public Reserva() {
    }

    public Reserva(LocalDate data, Aluno aluno) {
        this.data = data;
        this.aluno = aluno;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public List<ReservaLivro> getLivros() {
        return livros;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void setLivros(List<ReservaLivro> livros) {
        this.livros = livros;
    }
}