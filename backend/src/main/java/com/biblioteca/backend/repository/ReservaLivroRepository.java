package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.ReservaLivro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaLivroRepository extends JpaRepository<ReservaLivro, Long> {
}
