package com.agroflow.facturacion_service.dto;

import java.util.UUID;

public class EstadoCosechaRequest {

    private String estado;
    private UUID facturaId;

    // Constructores
    public EstadoCosechaRequest() {}

    public EstadoCosechaRequest(String estado, UUID facturaId) {
        this.estado = estado;
        this.facturaId = facturaId;
    }

    // Getters y Setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public UUID getFacturaId() { return facturaId; }
    public void setFacturaId(UUID facturaId) { this.facturaId = facturaId; }
}