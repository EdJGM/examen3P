package com.agroflow.central_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cosechas")
public class Cosecha {

    @Id
    @Column(name = "cosecha_id", columnDefinition = "UUID")
    private UUID cosechaId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agricultor_id", nullable = false)
    private Agricultor agricultor;

    @NotBlank(message = "El producto es obligatorio")
    @Size(max = 50, message = "El producto no puede exceder 50 caracteres")
    @Column(name = "producto", nullable = false, length = 50)
    private String producto;

    @NotNull(message = "Las toneladas son obligatorias")
    @DecimalMin(value = "0.0", inclusive = false, message = "Las toneladas deben ser mayor a 0")
    @Column(name = "toneladas", nullable = false, precision = 10, scale = 2)
    private BigDecimal toneladas;

    @Column(name = "estado", length = 20)
    private String estado = "REGISTRADA";

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "factura_id", columnDefinition = "UUID")
    private UUID facturaId;

    // Constructores
    public Cosecha() {}

    public Cosecha(Agricultor agricultor, String producto, BigDecimal toneladas) {
        this.agricultor = agricultor;
        this.producto = producto;
        this.toneladas = toneladas;
    }

    // Getters y Setters
    public UUID getCosechaId() { return cosechaId; }
    public void setCosechaId(UUID cosechaId) { this.cosechaId = cosechaId; }

    public Agricultor getAgricultor() { return agricultor; }
    public void setAgricultor(Agricultor agricultor) { this.agricultor = agricultor; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public BigDecimal getToneladas() { return toneladas; }
    public void setToneladas(BigDecimal toneladas) { this.toneladas = toneladas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public UUID getFacturaId() { return facturaId; }
    public void setFacturaId(UUID facturaId) { this.facturaId = facturaId; }
}