package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
