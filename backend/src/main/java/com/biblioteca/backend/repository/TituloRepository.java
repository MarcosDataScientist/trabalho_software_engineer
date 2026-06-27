package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

    Optional<Titulo> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
