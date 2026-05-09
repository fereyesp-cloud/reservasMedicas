package citas.medicas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import citas.medicas.DTO.CitaMedicaRequestDTO;
import citas.medicas.DTO.CitaMedicaResponseDTO;
import citas.medicas.model.CitaMedica;
import citas.medicas.model.EstadoCita;
import citas.medicas.repository.CitaMedicaRepository;
import citas.medicas.service.CitaMedicaServiceImpl;

class MedicasApplicationTests {

    @Mock
    private CitaMedicaRepository citaMedicaRepository;

    @InjectMocks
    private CitaMedicaServiceImpl citaMedicaService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    //  PRUEBA 1 
    @Test
    void crearCitaMedica_deberia_retornar_response() {

        CitaMedicaRequestDTO request = new CitaMedicaRequestDTO();
        request.setNombrePaciente("Juan Pérez");
        request.setNombreMedico("Dr. Rodríguez");
        request.setEspecialidad("Medicina General");
        request.setFechaCita(LocalDateTime.now().plusDays(1));

        CitaMedica citaGuardada = new CitaMedica();
        citaGuardada.setId(1L);
        citaGuardada.setNombrePaciente("Juan Pérez");
        citaGuardada.setNombreMedico("Dr. Rodríguez");
        citaGuardada.setEspecialidad("Medicina General");
        citaGuardada.setEstado(EstadoCita.PROGRAMADA);
        citaGuardada.setFechaCita(request.getFechaCita());

        when(citaMedicaRepository.save(any(CitaMedica.class))).thenReturn(citaGuardada);

        CitaMedicaResponseDTO response = citaMedicaService.createCitaMedica(request);

        assertNotNull(response);
        assertEquals("Juan Pérez", response.getNombrePaciente());
        assertEquals("Dr. Rodríguez", response.getNombreMedico());
        verify(citaMedicaRepository, times(1)).save(any(CitaMedica.class));
    }

    //  PRUEBA 2 
    @Test
    void getAllCitasMedicas_deberia_retornar_lista() {

        CitaMedica cita1 = new CitaMedica();
        cita1.setId(1L);
        cita1.setNombrePaciente("Juan Pérez");
        cita1.setEstado(EstadoCita.PROGRAMADA);

        CitaMedica cita2 = new CitaMedica();
        cita2.setId(2L);
        cita2.setNombrePaciente("María López");
        cita2.setEstado(EstadoCita.PROGRAMADA);

        when(citaMedicaRepository.findAll()).thenReturn(Arrays.asList(cita1, cita2));

        List<CitaMedicaResponseDTO> resultado = citaMedicaService.getAllCitasMedicas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaMedicaRepository, times(1)).findAll();
    }

    // PRUEBA 3 
    @Test
    void cancelarCita_deberia_cambiar_estado_a_cancelada() {

        CitaMedica cita = new CitaMedica();
        cita.setId(1L);
        cita.setNombrePaciente("Juan Pérez");
        cita.setNombreMedico("Dr. Rodríguez");
        cita.setEspecialidad("Medicina General");
        cita.setEstado(EstadoCita.PROGRAMADA);
        cita.setFechaCita(LocalDateTime.now().plusDays(1));

        when(citaMedicaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaMedicaRepository.save(any(CitaMedica.class))).thenReturn(cita);

        assertDoesNotThrow(() -> citaMedicaService.cancelarCita(1L));

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        verify(citaMedicaRepository, times(1)).findById(1L);
        verify(citaMedicaRepository, times(1)).save(cita);
    }

    // PRUEBA 4 
    @Test
    void getHorariosDisponibles_deberia_retornar_citas_disponibles() {

        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(7);

        CitaMedica cita1 = new CitaMedica();
        cita1.setId(1L);
        cita1.setNombreMedico("Dr. Rodríguez");
        cita1.setEspecialidad("Cardiología");
        cita1.setEstado(EstadoCita.DISPONIBLE);
        cita1.setFechaCita(LocalDateTime.now().plusDays(2));

        CitaMedica cita2 = new CitaMedica();
        cita2.setId(2L);
        cita2.setNombreMedico("Dra. Martínez");
        cita2.setEspecialidad("Pediatría");
        cita2.setEstado(EstadoCita.DISPONIBLE);
        cita2.setFechaCita(LocalDateTime.now().plusDays(5));

        when(citaMedicaRepository.findByEstadoAndFechaCitaBetween(
                EstadoCita.DISPONIBLE, inicio, fin))
                .thenReturn(Arrays.asList(cita1, cita2));

        List<CitaMedicaResponseDTO> resultado = citaMedicaService.getHorariosDisponibles(inicio, fin);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(EstadoCita.DISPONIBLE, resultado.get(0).getEstado());
        assertEquals(EstadoCita.DISPONIBLE, resultado.get(1).getEstado());
        verify(citaMedicaRepository, times(1))
                .findByEstadoAndFechaCitaBetween(EstadoCita.DISPONIBLE, inicio, fin);
    }
}