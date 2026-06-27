package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.TituloAutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloAutorRepository extends JpaRepository<TituloAutor, Long> {
}
