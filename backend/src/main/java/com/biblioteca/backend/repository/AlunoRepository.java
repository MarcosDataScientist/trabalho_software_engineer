package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    @Query("SELECT a FROM Aluno a LEFT JOIN FETCH a.debitos WHERE a.matricula = :matricula")
    Optional<Aluno> findByMatriculaWithDebitos(@Param("matricula") Long matricula);
}
