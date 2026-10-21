package com.example.U2A9SB.service;

import com.example.U2A9SB.dto.PrestamoResponseDTO;
import com.example.U2A9SB.dto.SolicitarPrestamoDTO;

import java.util.List;

public interface PrestamoService {
    PrestamoResponseDTO solicitarPrestamo(SolicitarPrestamoDTO dto);
    PrestamoResponseDTO devolverLibro(Long prestamoId);
    PrestamoResponseDTO renovarPrestamo(Long prestamoId);
    void rehabilitarLector(Long lectorId);
    List<PrestamoResponseDTO> listarPrestamosActivosLector(Long lectorId);
}