package com.biblioteca.backend.repository;

import com.biblioteca.backend.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query("""
            SELECT DISTINCT e FROM Emprestimo e
            JOIN FETCH e.aluno a
            JOIN FETCH e.itens i
            JOIN FETCH i.livro l
            JOIN FETCH l.titulo
            WHERE e.id = :id
            """)
    Optional<Emprestimo> findByIdParaDevolucao(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT e FROM Emprestimo e
            JOIN FETCH e.aluno a
            JOIN FETCH e.itens i
            JOIN FETCH i.livro l
            JOIN FETCH l.titulo
            WHERE e.aluno.matricula = :matricula AND e.dataDevolucao IS NULL
            """)
    List<Emprestimo> findEmprestimosAtivosByMatricula(@Param("matricula") Long matricula);
}
