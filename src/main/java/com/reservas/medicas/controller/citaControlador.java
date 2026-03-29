package com.reservas.medicas.controller;

import org.springframework.web.bind.annotation.*;

import com.reservas.medicas.model.cita;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;



@RestController
public class citaControlador {
    
    public List<cita> citas = new ArrayList<>();
    private int siguienteId= 9;


    public citaControlador(){
        // 8 citas precargadas
        citas.add(new cita(1, "Juan Pérez", "Dr. García",
                "Medicina General", "10/04/2026", "09:00",
                "PROGRAMADA", "Control rutinario"));

        citas.add(new cita(2, "María López", "Dra. Martínez",
                "Pediatría", "10/04/2026", "10:00",
                "PROGRAMADA", "Fiebre niño"));

        citas.add(new cita(3, "Carlos Ruiz", "Dr. Torres",
                "Cardiología", "11/04/2026", "09:30",
                "CANCELADA", "Revisión corazón"));

        citas.add(new cita(4, "Ana Soto", "Dra. Flores",
                "Dermatología", "11/04/2026", "11:00",
                "PROGRAMADA", "Alergia en piel"));

        citas.add(new cita(5, "Roberto Díaz", "Dr. García",
                "Medicina General", "12/04/2026", "08:30",
                "COMPLETADA", "Dolor de cabeza"));

        citas.add(new cita(6, "Lucía Mora", "Dr. Torres",
                "Cardiología", "12/04/2026", "10:30",
                "PROGRAMADA", "Chequeo anual"));

        citas.add(new cita(7, "Sofía Vega", "Dra. Martínez",
                "Pediatría", "13/04/2026", "09:00",
                "PROGRAMADA", "Vacuna anual"));

        citas.add(new cita(8, "Pedro Núñez", "Dra. Flores",
                "Dermatología", "13/04/2026", "11:30",
                "CANCELADA", "Revisión lunar"));
    }

    /**
     * GET: listar todas la citas
     */
   @GetMapping("/citas")
    public List<cita> obtenerTodasLasCitas() {  // ← sin parámetros
        return citas;
    }
    
    /**
     * buscar cita por ID
     */
    @GetMapping("/citas/{id}")
    public ResponseEntity<cita> obtenerCitaPorId(@PathVariable int id) {
        for (cita cita : citas) {
            if (cita.getId() == id) {
                return ResponseEntity.ok(cita);
            }
        }
        return ResponseEntity.notFound().build();
    }
 
    /**
     * GET: consultar disponibilidad por doctor y fecha
     * @param doctor
     * @param fecha
     * @return
     */
    @GetMapping("/citas/disponibilidad")
    public ResponseEntity<Object> consultarDisponibilidad(
            @RequestParam String doctor,
            @RequestParam String fecha) {

        // Validación: campos vacíos
        if (doctor.isBlank() || fecha.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Error: el doctor y la fecha no pueden estar vacíos.");
        }

        // Horas disponibles en el día
        List<String> horasDisponibles = new ArrayList<>();
        horasDisponibles.add("08:30");
        horasDisponibles.add("09:00");
        horasDisponibles.add("09:30");
        horasDisponibles.add("10:00");
        horasDisponibles.add("10:30");
        horasDisponibles.add("11:00");
        horasDisponibles.add("11:30");
        horasDisponibles.add("12:00");

        // Eliminar horas ya ocupadas
        for (cita cita : citas) {
            if (cita.getNombreDoctor().equalsIgnoreCase(doctor) &&
                cita.getFecha().equals(fecha) &&
                cita.getEstado().equalsIgnoreCase("PROGRAMADA")) {
                horasDisponibles.remove(cita.getHora());
            }
        }

        if (horasDisponibles.isEmpty()) {
            return ResponseEntity.ok("No hay horas disponibles para el Dr/Dra. "
                    + doctor + " el día " + fecha);
        }

        return ResponseEntity.ok(horasDisponibles);
    }

 
    /**
     * GET: filtrar citas por estado
     * @param estado
     * @return
     */
    @GetMapping("/citas/estado/{estado}")
    public ResponseEntity<List<cita>> obtenerPorEstado(@PathVariable String estado) {

        if (!estado.equalsIgnoreCase("PROGRAMADA") &&
            !estado.equalsIgnoreCase("CANCELADA") &&
            !estado.equalsIgnoreCase("COMPLETADA")) {
            return ResponseEntity.badRequest().build();
        }

        List<cita> resultado = new ArrayList<>();
        for (cita cita : citas) {
            if (cita.getEstado().equalsIgnoreCase(estado)) {
                resultado.add(cita);
            }
        }
        return ResponseEntity.ok(resultado);
    }

  
    /**
     * GET: programar nueva cita
     * @param nombrePaciente
     * @param nombreDoctor
     * @param especialidad
     * @param fecha
     * @param hora
     * @param motivo
     * @return
     */
    @GetMapping("/citas/nueva")
    public ResponseEntity<String> programarCita(
            @RequestParam String nombrePaciente,
            @RequestParam String nombreDoctor,
            @RequestParam String especialidad,
            @RequestParam String fecha,
            @RequestParam String hora,
            @RequestParam String motivo) {

        // Validación: campos vacíos
        if (nombrePaciente.isBlank() || nombreDoctor.isBlank() ||
            especialidad.isBlank() || fecha.isBlank() ||
            hora.isBlank() || motivo.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Error: ningún campo puede estar vacío.");
        }

        // Validación: doctor ya ocupado en esa fecha y hora
        for (cita cita : citas) {
            if (cita.getNombreDoctor().equalsIgnoreCase(nombreDoctor) &&
                cita.getFecha().equals(fecha) &&
                cita.getHora().equals(hora) &&
                cita.getEstado().equalsIgnoreCase("PROGRAMADA")) {
                return ResponseEntity.badRequest()
                        .body("Error: el Dr/Dra. " + nombreDoctor +
                              " ya tiene una cita programada el " + fecha +
                              " a las " + hora);
            }
        }

        citas.add(new cita(siguienteId++, nombrePaciente, nombreDoctor,
                especialidad, fecha, hora, "PROGRAMADA", motivo));

        return ResponseEntity.ok("Cita programada con éxito. ID asignado: "
                + (siguienteId - 1));
    }

    /**
     * GET: cancelar cita por ID
     * @param id
     * @return
     */
    @GetMapping("/citas/cancelar/{id}")
    public ResponseEntity<String> cancelarCita(@PathVariable int id) {
        for (cita cita : citas) {
            if (cita.getId() == id) {

                // Validación: ya cancelada
                if (cita.getEstado().equalsIgnoreCase("CANCELADA")) {
                    return ResponseEntity.badRequest()
                            .body("Error: la cita ID " + id + " ya está cancelada.");
                }

                // Validación: ya completada
                if (cita.getEstado().equalsIgnoreCase("COMPLETADA")) {
                    return ResponseEntity.badRequest()
                            .body("Error: no se puede cancelar una cita ya completada.");
                }

                cita.setEstado("CANCELADA");
                return ResponseEntity.ok("Cita ID " + id + " cancelada correctamente.");
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET: filtrar citas por doctor
     * @param nombreDoctor
     * @return
     */
    @GetMapping("/citas/doctor")
    public ResponseEntity<List<cita>> obtenerCitasPorDoctor(
            @RequestParam String nombreDoctor) {

        if (nombreDoctor.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<cita> resultado = new ArrayList<>();
        for (cita cita : citas) {
            if (cita.getNombreDoctor().equalsIgnoreCase(nombreDoctor)) {
                resultado.add(cita);
            }
        }
        return ResponseEntity.ok(resultado);
    }
    
    
}
