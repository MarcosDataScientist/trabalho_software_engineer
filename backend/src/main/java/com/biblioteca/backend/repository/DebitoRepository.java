package com.biblioteca.backend.repository;

import com.model.Debito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebitoRepository extends JpaRepository<Debito, Long> {
}
