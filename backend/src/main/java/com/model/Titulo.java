package com.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titulo")
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_titulo")
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false)
    private Integer prazo;

    @Column(unique = true, length = 20)
    private String isbn;

    private Integer edicao;

    private Integer ano;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_area", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "titulo")
    private List<Livro> livros = new ArrayList<>();

    @OneToMany(mappedBy = "titulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TituloAutor> autores = new ArrayList<>();

    public Titulo() {
    }

    public Titulo(String titulo, Integer prazo, String isbn, Integer edicao, Integer ano, Area area) {
        this.titulo = titulo;
        this.prazo = prazo;
        this.isbn = isbn;
        this.edicao = edicao;
        this.ano = ano;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getPrazo() {
        return prazo;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getEdicao() {
        return edicao;
    }

    public Integer getAno() {
        return ano;
    }

    public Area getArea() {
        return area;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<TituloAutor> getAutores() {
        return autores;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPrazo(Integer prazo) {
        this.prazo = prazo;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public void setAutores(List<TituloAutor> autores) {
        this.autores = autores;
    }

    public LocalDate calcularDataPrevistaDevolucao(LocalDate dataEmprestimo) {
        return dataEmprestimo.plusDays(this.prazo);
    }
}