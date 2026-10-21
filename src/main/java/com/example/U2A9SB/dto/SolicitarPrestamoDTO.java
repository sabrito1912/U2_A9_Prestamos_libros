package com.example.U2A9SB.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitarPrestamoDTO {

    @NotNull(message = "El ID del lector es obligatorio")
    private Long lectorId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;
}