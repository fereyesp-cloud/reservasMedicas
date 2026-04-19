package citas.medicas.DTO;

import java.time.LocalDateTime;

import citas.medicas.model.EstadoCita;
import lombok.Data;

@Data
public class CitaMedicaResponseDTO {

    private Long id;
    private LocalDateTime fechaCita;
    private String nombrePaciente;
    private String nombreMedico;
    private String especialidad;
    private EstadoCita estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
}