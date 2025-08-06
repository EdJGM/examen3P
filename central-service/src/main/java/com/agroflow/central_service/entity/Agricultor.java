package com.agroflow.central_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "agricultores")
public class Agricultor {

    @Id
    @Column(name = "agricultor_id", columnDefinition = "UUID")
    private UUID agricultorId = UUID.randomUUID();

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La finca es obligatoria")
    @Size(max = 100, message = "La finca no puede exceder 100 caracteres")
    @Column(name = "finca", nullable = false, length = 100)
    private String finca;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    @Column(name = "ubicacion", nullable = false, length = 100)
    private String ubicacion;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "agricultor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cosecha> cosechas;

    // Constructores
    public Agricultor() {}

    public Agricultor(String nombre, String finca, String ubicacion, String correo) {
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

    public List<Cosecha> getCosechas() { return cosechas; }
    public void setCosechas(List<Cosecha> cosechas) { this.cosechas = cosechas; }
}