package citas.medicas.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CitaMedicaRequestDTO {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 100, message = "El nombre del paciente no puede superar los 100 caracteres")
    private String nombrePaciente;

    @NotBlank(message = "El nombre del médico es obligatorio")
    @Size(max = 100, message = "El nombre del médico no puede superar los 100 caracteres")
    private String nombreMedico;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @Future(message = "La fecha de la cita debe ser una fecha futura")
    private LocalDateTime fechaCita;
}