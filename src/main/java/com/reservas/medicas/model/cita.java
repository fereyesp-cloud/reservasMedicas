package com.reservas.medicas.model;


public class cita {
    
    /** Identificador único */
    private int id;
    /**Nombre del paciente */
    private String nombrePaciente;
    /**Nombre del doctor */
    private String nombreDoctor;
    /**nombre de la especialidad */
    private String especialidad;
    /**fecha de la consulta */
    private String fecha;   // formato: DD/MM/YYYY
    /** hora de la consulta */
    private String hora;    // formato: HH:MM
    /** El estado de la cita */
    private String estado;  // PROGRAMADA, CANCELADA, COMPLETADA
    /**Motivo de la cita */
    private String motivo;

        /**
     * Constructor completo para la cita medica
     *
     * @param id       Nombre del producto
     * @param nombrePaciente  NNombre del producto
     * @param nombreDoctor      Nombre del dueño
     * @param especialidad Dirrecion del envio
     * @param fecha   Estado del envio*
     * @param hora  Ubicacion del producto
     * @param motivo Codigo del seguimiento
     */

        public cita(int id, String nombrePaciente, String nombreDoctor,
                String especialidad, String fecha, String hora,
                String estado, String motivo) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.nombreDoctor = nombreDoctor;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.motivo = motivo;
    }

   // getters
    public int getId() { 
        return id; 
    }
    public String getNombrePaciente() {
         return nombrePaciente; 
    }
    public String getNombreDoctor() {
         return nombreDoctor; 
    }
    public String getEspecialidad() { 
        return especialidad; 
    }
    public String getFecha() { 
        return fecha; 
    }
    public String getHora() { 
        return hora; 
    }
    public String getEstado() { 
        return estado; 
    }
    public String getMotivo() { 
        return motivo; 
    }


    // Setters
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
    public void setFecha(String fecha) { 
        this.fecha = fecha; 
    }
    public void setHora(String hora) { this.hora = hora; }
}
