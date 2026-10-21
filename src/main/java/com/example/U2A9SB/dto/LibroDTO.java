package com.example.U2A9SB.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LibroDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @Min(value = 0, message = "Los ejemplares disponibles no pueden ser negativos")
    private int ejemplaresDisponibles;
}