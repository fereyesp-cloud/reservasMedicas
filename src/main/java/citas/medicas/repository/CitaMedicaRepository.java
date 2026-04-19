package citas.medicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import citas.medicas.model.CitaMedica;
import citas.medicas.model.EstadoCita;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    // Buscar cita por fecha exacta
    Optional<CitaMedica> findByFechaCita(LocalDateTime fechaCita);

    // Consultar disponibilidad: citas en un rango de fechas con estado DISPONIBLE
    List<CitaMedica> findByEstadoAndFechaCitaBetween(
        EstadoCita estado,
        LocalDateTime inicio,
        LocalDateTime fin
    );

    // Buscar citas de un paciente específico
    List<CitaMedica> findByNombrePaciente(String nombrePaciente);
}