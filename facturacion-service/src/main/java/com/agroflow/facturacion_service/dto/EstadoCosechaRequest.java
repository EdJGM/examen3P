package com.agroflow.facturacion_service.dto;

import java.util.UUID;

public class EstadoCosechaRequest {

    private String estado;
    private UUID factura_id;

    // Constructores
    public EstadoCosechaRequest() {}

    public EstadoCosechaRequest(String estado, UUID facturaId) {
        this.estado = estado;
        this.factura_id = factura_id;
    }

    // Getters y Setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public UUID getFacturaId() { return factura_id; }
    public void setFacturaId(UUID facturaId) { this.factura_id = facturaId; }
}