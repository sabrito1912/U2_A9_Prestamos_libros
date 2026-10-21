package com.example.U2A9SB.controller;

import com.example.U2A9SB.dto.LectorDTO;
import com.example.U2A9SB.model.Lector;
import com.example.U2A9SB.repository.LectorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectores")
@RequiredArgsConstructor
public class LectorController {

    private final LectorRepository lectorRepository;

    @PostMapping
    public ResponseEntity<Lector> crearLector(@Valid @RequestBody LectorDTO dto) {
        Lector lector = Lector.builder()
                .nombre(dto.getNombre())
                .activo(true)
                .membresiaPremium(dto.isMembresiaPremium())
                .build();
        return new ResponseEntity<>(lectorRepository.save(lector), HttpStatus.CREATED);
    }
}