package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Debito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebitoRepository extends JpaRepository<Debito, Long> {
}
