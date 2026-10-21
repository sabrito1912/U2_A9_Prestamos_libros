package com.example.U2A9SB.service.impl;

import com.example.U2A9SB.dto.PrestamoResponseDTO;
import com.example.U2A9SB.dto.SolicitarPrestamoDTO;
import com.example.U2A9SB.exception.BusinessRuleException;
import com.example.U2A9SB.exception.ResourceNotFoundException;
import com.example.U2A9SB.model.EstadoPrestamo;
import com.example.U2A9SB.model.Lector;
import com.example.U2A9SB.model.Libro;
import com.example.U2A9SB.model.Prestamo;
import com.example.U2A9SB.repository.LectorRepository;
import com.example.U2A9SB.repository.LibroRepository;
import com.example.U2A9SB.repository.PrestamoRepository;
import com.example.U2A9SB.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    @Override
    @Transactional
    public PrestamoResponseDTO solicitarPrestamo(SolicitarPrestamoDTO dto) {
        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new ResourceNotFoundException("El lector no existe"));

        if (!lector.isActivo()) {
            throw new BusinessRuleException("El lector se encuentra inactivo/bloqueado");
        }

        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("El libro no existe"));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new BusinessRuleException("El libro no cuenta con ejemplares disponibles");
        }

        long prestamosActivos = prestamoRepository.countByLectorAndEstadoIn(
                lector, List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO));

        int limiteMaximo = lector.isMembresiaPremium() ? 5 : 3;

        if (prestamosActivos >= limiteMaximo) {
            throw new BusinessRuleException("El lector ha alcanzado el límite máximo de préstamos activos (" + limiteMaximo + ")");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = Prestamo.builder()
                .lector(lector)
                .libro(libro)
                .fechaPrestamo(LocalDate.now())
                .fechaDevolucionMaxima(LocalDate.now().plusDays(14))
                .estado(EstadoPrestamo.PRESTADO)
                .build();

        return mapToDTO(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional
    public PrestamoResponseDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BusinessRuleException("El préstamo ya ha sido devuelto previamente");
        }

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        return mapToDTO(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional
    public PrestamoResponseDTO renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BusinessRuleException("No se puede renovar un préstamo en estado " + prestamo.getEstado());
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return mapToDTO(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional
    public void rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new ResourceNotFoundException("El lector no existe"));

        lector.setActivo(true);
        lectorRepository.save(lector);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new ResourceNotFoundException("El lector no existe");
        }

        List<Prestamo> prestamos = prestamoRepository.findByLectorIdAndEstadoIn(
                lectorId, List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO));

        return prestamos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private PrestamoResponseDTO mapToDTO(Prestamo prestamo) {
        return PrestamoResponseDTO.builder()
                .id(prestamo.getId())
                .lectorId(prestamo.getLector().getId())
                .lectorNombre(prestamo.getLector().getNombre())
                .libroId(prestamo.getLibro().getId())
                .libroTitulo(prestamo.getLibro().getTitulo())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima())
                .estado(prestamo.getEstado())
                .build();
    }
}