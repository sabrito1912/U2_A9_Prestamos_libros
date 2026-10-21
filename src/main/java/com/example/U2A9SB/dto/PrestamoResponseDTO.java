package com.example.U2A9SB.dto;

import com.example.U2A9SB.model.EstadoPrestamo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PrestamoResponseDTO {
    private Long id;
    private Long lectorId;
    private String lectorNombre;
    private Long libroId;
    private String libroTitulo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private EstadoPrestamo estado;
}