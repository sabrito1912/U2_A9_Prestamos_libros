package com.example.U2A9SB.controller;

import com.example.U2A9SB.dto.LibroDTO;
import com.example.U2A9SB.model.Libro;
import com.example.U2A9SB.repository.LibroRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroRepository libroRepository;

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody LibroDTO dto) {
        Libro libro = Libro.builder()
                .titulo(dto.getTitulo())
                .ejemplaresDisponibles(dto.getEjemplaresDisponibles())
                .build();
        return new ResponseEntity<>(libroRepository.save(libro), HttpStatus.CREATED);
    }
}