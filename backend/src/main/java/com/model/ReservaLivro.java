
package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reserva_livro")
public class ReservaLivro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_livro")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    public ReservaLivro() {
    }

    public ReservaLivro(Reserva reserva, Livro livro) {
        this.reserva = reserva;
        this.livro = livro;
    }

    public Long getId() {
        return id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }
}