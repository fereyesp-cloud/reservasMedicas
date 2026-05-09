package citas.medicas.exception;

public class CitaNotFoundException extends RuntimeException {
    public CitaNotFoundException(String mensaje) {
        super(mensaje);
    }
}