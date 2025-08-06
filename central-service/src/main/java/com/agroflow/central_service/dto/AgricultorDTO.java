package com.agroflow.central_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgricultorDTO {

    private UUID agricultorId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "La finca es obligatoria")
    @Size(max = 100, message = "La finca no puede exceder 100 caracteres")
    private String finca;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String ubicacion;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correo;

    private LocalDateTime fechaRegistro;

    // Constructores
    public AgricultorDTO() {}

    public AgricultorDTO(String nombre, String finca, String ubicacion, String correo) {
        this.nombre = nombre;
        this.finca = finca;
        this.ubicacion = ubicacion;
        this.correo = correo;
    }

    // Getters y Setters
    public UUID getAgricultorId() { return agricultorId; }
    public void setAgricultorId(UUID agricultorId) { this.agricultorId = agricultorId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFinca() { return finca; }
    public void setFinca(String finca) { this.finca = finca; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}