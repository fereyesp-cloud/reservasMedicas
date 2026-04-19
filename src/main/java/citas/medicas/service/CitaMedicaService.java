package citas.medicas.service;

import java.time.LocalDateTime;
import java.util.List;

import citas.medicas.DTO.ActualizarCitaDTO;
import citas.medicas.DTO.CitaMedicaRequestDTO;
import citas.medicas.DTO.CitaMedicaResponseDTO;


public interface CitaMedicaService {

    // Listar todas las citas
    List<CitaMedicaResponseDTO> getAllCitasMedicas();

    // Programar una nueva cita
    CitaMedicaResponseDTO createCitaMedica(CitaMedicaRequestDTO requestDTO);

    // Actualizar estado o fecha de una cita
    CitaMedicaResponseDTO updateCitaMedica(Long id, ActualizarCitaDTO actualizarCitaDTO);

    // Consultar horarios disponibles en un rango de fechas
    List<CitaMedicaResponseDTO> getHorariosDisponibles(LocalDateTime inicio, LocalDateTime fin);

    // Cancelar una cita por id
    void cancelarCita(Long id);
}