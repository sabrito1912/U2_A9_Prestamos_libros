package com.example.U2A9SB.repository;

import com.example.U2A9SB.model.EstadoPrestamo;
import com.example.U2A9SB.model.Lector;
import com.example.U2A9SB.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByLectorAndEstadoIn(Lector lector, List<EstadoPrestamo> estados);
    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
}