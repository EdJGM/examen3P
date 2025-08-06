package com.agroflow.central_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CosechaDTO {

    private UUID cosechaId;

    @NotNull(message = "El ID del agricultor es obligatorio")
    private UUID agricultorId;

    @NotBlank(message = "El producto es obligatorio")
    @Size(max = 50, message = "El producto no puede exceder 50 caracteres")
    private String producto;

    @NotNull(message = "Las toneladas son obligatorias")
    @DecimalMin(value = "0.0", inclusive = false, message = "Las toneladas deben ser mayor a 0")
    private BigDecimal toneladas;

    private String estado;
    private LocalDateTime creadoEn;
    private UUID facturaId;
    private String ubicacion; // Para el ejemplo del documento

    // Constructores
    public CosechaDTO() {}

    public CosechaDTO(UUID agricultorId, String producto, BigDecimal toneladas, String ubicacion) {
        this.agricultorId = agricultorId;
        this.producto = producto;
        this.toneladas = toneladas;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public UUID getCosechaId() { return cosechaId; }
    public void setCosechaId(UUID cosechaId) { this.cosechaId = cosechaId; }

    public UUID getAgricultorId() { return agricultorId; }
    public void setAgricultorId(UUID agricultorId) { this.agricultorId = agricultorId; }

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

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}