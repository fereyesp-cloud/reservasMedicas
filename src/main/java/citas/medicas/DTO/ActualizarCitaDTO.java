package citas.medicas.DTO;

import java.time.LocalDateTime;

import citas.medicas.model.EstadoCita;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarCitaDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoCita estado;

    @Future(message = "La nueva fecha debe ser una fecha futura")
    private LocalDateTime fechaCita;
}