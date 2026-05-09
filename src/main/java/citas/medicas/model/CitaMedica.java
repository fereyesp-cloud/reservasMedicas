package citas.medicas.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "citas")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_citas")
    @SequenceGenerator(name = "seq_citas", sequenceName = "SEQ_CITAS", allocationSize = 1)
    private Long id;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "nombre_paciente", nullable = false)
    private String nombrePaciente;

    @Column(name = "nombre_medico", nullable = false)
    private String nombreMedico;

    @Column(name = "especialidad", nullable = false)
    private String especialidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void antesDeGuardar() {
        this.fechaRegistro = LocalDateTime.now();
        this.estado = EstadoCita.PROGRAMADA;
    }

    @PreUpdate
    public void antesDeActualizar() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}