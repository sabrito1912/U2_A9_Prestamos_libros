package com.example.U2A9SB.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LectorDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private boolean membresiaPremium;
}