package com.agroflow.facturacion_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EventoCosechaDTO {

    private UUID eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private PayloadCosecha payload;

    // Constructores
    public EventoCosechaDTO() {}

    // Getters y Setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public PayloadCosecha getPayload() { return payload; }
    public void setPayload(PayloadCosecha payload) { this.payload = payload; }

    // Clase interna para el payload
    public static class PayloadCosecha {
        private UUID cosechaId;
        private String producto;
        private BigDecimal toneladas;
        private List<String> requiereInsumos;

        // Constructores
        public PayloadCosecha() {}

        // Getters y Setters
        public UUID getCosechaId() { return cosechaId; }
        public void setCosechaId(UUID cosechaId) { this.cosechaId = cosechaId; }

        public String getProducto() { return producto; }
        public void setProducto(String producto) { this.producto = producto; }

        public BigDecimal getToneladas() { return toneladas; }
        public void setToneladas(BigDecimal toneladas) { this.toneladas = toneladas; }

        public List<String> getRequiereInsumos() { return requiereInsumos; }
        public void setRequiereInsumos(List<String> requiereInsumos) { this.requiereInsumos = requiereInsumos; }
    }
}