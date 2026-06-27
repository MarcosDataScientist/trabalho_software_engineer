package com.biblioteca.backend.repository;

import com.model.ReservaLivro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaLivroRepository extends JpaRepository<ReservaLivro, Long> {
}
