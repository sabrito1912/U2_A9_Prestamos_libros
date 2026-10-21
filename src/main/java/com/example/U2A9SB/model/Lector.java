package com.example.U2A9SB.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lectores")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private boolean activo = true;

    private boolean membresiaPremium = false;
}