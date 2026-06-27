
package com.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Long id;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @Column(name = "exemplar_biblioteca", nullable = false)
    private Boolean exemplarBiblioteca = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_titulo", nullable = false)
    private Titulo titulo;

    @OneToMany(mappedBy = "livro")
    private List<ItemEmprestimo> itensEmprestimo = new ArrayList<>();

    @OneToMany(mappedBy = "livro")
    private List<ReservaLivro> reservas = new ArrayList<>();

    public Livro() {
    }

    public Livro(Boolean disponivel, Boolean exemplarBiblioteca, Titulo titulo) {
        this.disponivel = disponivel;
        this.exemplarBiblioteca = exemplarBiblioteca;
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public Boolean getExemplarBiblioteca() {
        return exemplarBiblioteca;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public List<ItemEmprestimo> getItensEmprestimo() {
        return itensEmprestimo;
    }

    public List<ReservaLivro> getReservas() {
        return reservas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setExemplarBiblioteca(Boolean exemplarBiblioteca) {
        this.exemplarBiblioteca = exemplarBiblioteca;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public void setItensEmprestimo(List<ItemEmprestimo> itensEmprestimo) {
        this.itensEmprestimo = itensEmprestimo;
    }

    public void setReservas(List<ReservaLivro> reservas) {
        this.reservas = reservas;
    }

    public boolean podeSerEmprestado() {
        return Boolean.TRUE.equals(disponivel)
                && Boolean.TRUE.equals(exemplarBiblioteca)
                && !possuiReservaAtiva();
    }

    public boolean possuiReservaAtiva() {
        return reservas != null && !reservas.isEmpty();
    }

    public String getMotivoImpedimentoEmprestimo() {
        if (!Boolean.TRUE.equals(disponivel)) {
            return "Livro indisponível para empréstimo (id " + id + ")";
        }
        if (!Boolean.TRUE.equals(exemplarBiblioteca)) {
            return "Exemplar não permitido para empréstimo (id " + id + ")";
        }
        if (possuiReservaAtiva()) {
            return "Livro possui reserva ativa (id " + id + ")";
        }
        return "Livro não pode ser emprestado (id " + id + ")";
    }

    public void marcarComoIndisponivel() {
        this.disponivel = false;
    }
}