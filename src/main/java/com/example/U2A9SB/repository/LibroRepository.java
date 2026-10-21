package com.example.U2A9SB.repository;

import com.example.U2A9SB.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}