package com.example.U2A9SB.controller;

import com.example.U2A9SB.dto.PrestamoResponseDTO;
import com.example.U2A9SB.dto.SolicitarPrestamoDTO;
import com.example.U2A9SB.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping("/solicitar")
    public ResponseEntity<PrestamoResponseDTO> solicitarPrestamo(@Valid @RequestBody SolicitarPrestamoDTO dto) {
        return new ResponseEntity<>(prestamoService.solicitarPrestamo(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<PrestamoResponseDTO> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(id));
    }

    @PutMapping("/lectores/{id}/rehabilitar")
    public ResponseEntity<String> rehabilitarLector(@PathVariable Long id) {
        prestamoService.rehabilitarLector(id);
        return ResponseEntity.ok("Lector rehabilitado correctamente.");
    }

    @GetMapping("/lectores/{id}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamosActivosLector(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(id));
    }
}