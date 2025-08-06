package com.agroflow.facturacion_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @Column(name = "factura_id", columnDefinition = "UUID")
    private UUID facturaId = UUID.randomUUID();

    @NotNull(message = "El ID de cosecha es obligatorio")
    @Column(name = "cosecha_id", nullable = false, columnDefinition = "UUID")
    private UUID cosechaId;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "pagado", nullable = false)
    private Boolean pagado = false;

    @CreationTimestamp
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "metodo_pago", length = 30)
    private String metodoPago;

    @Column(name = "codigo_qr", columnDefinition = "TEXT")
    private String codigoQr;

    // Constructores
    public Factura() {}

    public Factura(UUID cosechaId, BigDecimal montoTotal) {
        this.cosechaId = cosechaId;
        this.montoTotal = montoTotal;
    }

    // Getters y Setters
    public UUID getFacturaId() { return facturaId; }
    public void setFacturaId(UUID facturaId) { this.facturaId = facturaId; }

    public UUID getCosechaId() { return cosechaId; }
    public void setCosechaId(UUID cosechaId) { this.cosechaId = cosechaId; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public Boolean getPagado() { return pagado; }
    public void setPagado(Boolean pagado) { this.pagado = pagado; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
}