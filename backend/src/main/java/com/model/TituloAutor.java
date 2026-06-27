package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "titulo_autor")
public class TituloAutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_titulo_autor")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_titulo", nullable = false)
    private Titulo titulo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_autor", nullable = false)
    private Autor autor;

    public TituloAutor() {
    }

    public TituloAutor(Titulo titulo, Autor autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}