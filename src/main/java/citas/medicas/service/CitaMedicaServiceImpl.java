package citas.medicas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import citas.medicas.DTO.ActualizarCitaDTO;
import citas.medicas.DTO.CitaMedicaRequestDTO;
import citas.medicas.DTO.CitaMedicaResponseDTO;
import citas.medicas.exception.CitaNotFoundException;
import citas.medicas.model.CitaMedica;
import citas.medicas.model.EstadoCita;
import citas.medicas.repository.CitaMedicaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CitaMedicaServiceImpl implements CitaMedicaService {

    // ✅ CORRECCIÓN 1: Inyección por constructor en vez de @Autowired
    private final CitaMedicaRepository citaMedicaRepository;

    public CitaMedicaServiceImpl(CitaMedicaRepository citaMedicaRepository) {
        this.citaMedicaRepository = citaMedicaRepository;
    }

    @Override
    public CitaMedicaResponseDTO createCitaMedica(CitaMedicaRequestDTO requestDTO) {
        log.info("Registrando nueva cita médica para: {}", requestDTO.getNombrePaciente());

        CitaMedica citaMedica = new CitaMedica();
        citaMedica.setFechaCita(requestDTO.getFechaCita());
        citaMedica.setNombreMedico(requestDTO.getNombreMedico());
        citaMedica.setEspecialidad(requestDTO.getEspecialidad());
        citaMedica.setNombrePaciente(requestDTO.getNombrePaciente());

        CitaMedica guardada = citaMedicaRepository.save(citaMedica);
        log.info("Cita médica registrada con ID: {}", guardada.getId());
        return convertirAResponse(guardada);
    }

    @Override
    public CitaMedicaResponseDTO updateCitaMedica(Long id, ActualizarCitaDTO actualizarCitaDTO) {
        log.info("Actualizando cita médica con ID: {}", id);

        // ✅ CORRECCIÓN 2: Excepción personalizada en vez de RuntimeException
        CitaMedica citaMedica = citaMedicaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cita médica no encontrada con ID: {}", id);
                    return new CitaNotFoundException("Cita no encontrada con ID: " + id);
                });

        citaMedica.setEstado(actualizarCitaDTO.getEstado());
        if (actualizarCitaDTO.getFechaCita() != null) {
            citaMedica.setFechaCita(actualizarCitaDTO.getFechaCita());
        }

        CitaMedica actualizada = citaMedicaRepository.save(citaMedica);
        log.info("Cita médica actualizada correctamente: {}", id);
        return convertirAResponse(actualizada);
    }

    @Override
    public List<CitaMedicaResponseDTO> getAllCitasMedicas() {
        log.info("Consultando todas las citas médicas");

        List<CitaMedica> citas = citaMedicaRepository.findAll();
        log.debug("Total citas encontradas: {}", citas.size());

        // ✅ CORRECCIÓN 3: .toList() en vez de .collect(Collectors.toList())
        return citas.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public List<CitaMedicaResponseDTO> getHorariosDisponibles(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Consultando horarios disponibles entre {} y {}", inicio, fin);

        List<CitaMedica> disponibles = citaMedicaRepository
                .findByEstadoAndFechaCitaBetween(EstadoCita.DISPONIBLE, inicio, fin);

        log.debug("Horarios disponibles encontrados: {}", disponibles.size());

        // ✅ CORRECCIÓN 3: .toList() en vez de .collect(Collectors.toList())
        return disponibles.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public void cancelarCita(Long id) {
        log.warn("Cancelando cita médica con ID: {}", id);

        CitaMedica citaMedica = citaMedicaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se pudo cancelar. Cita no encontrada con ID: {}", id);
                    return new CitaNotFoundException("Cita no encontrada con ID: " + id);
                });

        citaMedica.setEstado(EstadoCita.CANCELADA);
        citaMedicaRepository.save(citaMedica);
        log.warn("Cita médica cancelada correctamente: {}", id);
    }

    private CitaMedicaResponseDTO convertirAResponse(CitaMedica citaMedica) {
        CitaMedicaResponseDTO response = new CitaMedicaResponseDTO();
        response.setId(citaMedica.getId());
        response.setFechaCita(citaMedica.getFechaCita());
        response.setNombrePaciente(citaMedica.getNombrePaciente());
        response.setNombreMedico(citaMedica.getNombreMedico());
        response.setEspecialidad(citaMedica.getEspecialidad());
        response.setEstado(citaMedica.getEstado());
        response.setFechaRegistro(citaMedica.getFechaRegistro());
        response.setFechaActualizacion(citaMedica.getFechaActualizacion());
        return response;
    }
}