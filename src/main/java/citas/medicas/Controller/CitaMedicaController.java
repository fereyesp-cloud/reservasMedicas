package citas.medicas.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import citas.medicas.DTO.ActualizarCitaDTO;
import citas.medicas.DTO.CitaMedicaRequestDTO;
import citas.medicas.DTO.CitaMedicaResponseDTO;
import citas.medicas.service.CitaMedicaService;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "*")
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    public CitaMedicaController(CitaMedicaService citaMedicaService) {
        this.citaMedicaService = citaMedicaService;
    }

    // GET - Listar todas las citas
    @GetMapping
    public ResponseEntity<List<CitaMedicaResponseDTO>> getAllCitasMedicas() {
        return ResponseEntity.ok(citaMedicaService.getAllCitasMedicas());
    }

    // GET - Consultar horarios disponibles en un rango de fechas
    @GetMapping("/disponibles")
    public ResponseEntity<List<CitaMedicaResponseDTO>> getHorariosDisponibles(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        return ResponseEntity.ok(citaMedicaService.getHorariosDisponibles(inicio, fin));
    }

    // POST - Programar nueva cita
    @PostMapping
    public ResponseEntity<CitaMedicaResponseDTO> createCitaMedica(
            @Valid @RequestBody CitaMedicaRequestDTO requestDTO) {
        CitaMedicaResponseDTO response = citaMedicaService.createCitaMedica(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT - Actualizar estado o fecha de una cita
    @PutMapping("/{id}")
    public ResponseEntity<CitaMedicaResponseDTO> updateCitaMedica(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCitaDTO actualizarCitaDTO) {
        CitaMedicaResponseDTO response = citaMedicaService.updateCitaMedica(id, actualizarCitaDTO);
        return ResponseEntity.ok(response);
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelarCita(@PathVariable Long id) {
        citaMedicaService.cancelarCita(id);
        return ResponseEntity.ok(Map.of("mensaje", "Cita cancelada correctamente"));
    }
}