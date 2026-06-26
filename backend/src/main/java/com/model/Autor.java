package com.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String sobrenome;

    @Column(length = 100)
    private String titulacao;

    @OneToMany(mappedBy = "autor")
    private List<TituloAutor> titulos = new ArrayList<>();

    public Autor() {
    }

    public Autor(String nome, String sobrenome, String titulacao) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.titulacao = titulacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getTitulacao() {
        return titulacao;
    }

    public List<TituloAutor> getTitulos() {
        return titulos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public void setTitulos(List<TituloAutor> titulos) {
        this.titulos = titulos;
    }
}