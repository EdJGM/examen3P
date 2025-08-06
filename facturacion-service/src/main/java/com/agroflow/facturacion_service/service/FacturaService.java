package com.agroflow.facturacion_service.service;

import com.agroflow.facturacion_service.dto.FacturaDTO;
import com.agroflow.facturacion_service.entity.Factura;
import com.agroflow.facturacion_service.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private CentralServiceClient centralServiceClient;

    // Tabla de precios de referencia (USD por tonelada)
    private static final Map<String, BigDecimal> PRECIOS = new HashMap<>();

    static {
        PRECIOS.put("Arroz", BigDecimal.valueOf(120));
        PRECIOS.put("Arroz Oro", BigDecimal.valueOf(120));
        PRECIOS.put("Café Premium", BigDecimal.valueOf(300));
        PRECIOS.put("Café", BigDecimal.valueOf(250));
        PRECIOS.put("Maíz", BigDecimal.valueOf(180));
        PRECIOS.put("Banano", BigDecimal.valueOf(90));
    }

    public List<FacturaDTO> obtenerTodas() {
        return facturaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<FacturaDTO> obtenerPorId(UUID id) {
        return facturaRepository.findById(id)
                .map(this::convertirADTO);
    }

    public Optional<FacturaDTO> obtenerPorCosechaId(UUID cosechaId) {
        return facturaRepository.findByCosechaId(cosechaId)
                .map(this::convertirADTO);
    }

    public FacturaDTO procesarCosecha(UUID cosechaId, String producto, BigDecimal toneladas) {
        // Verificar si ya existe factura para esta cosecha
        if (facturaRepository.findByCosechaId(cosechaId).isPresent()) {
            throw new RuntimeException("Ya existe una factura para esta cosecha");
        }

        // Calcular monto basado en la tabla de precios
        BigDecimal precioBase = PRECIOS.getOrDefault(producto, BigDecimal.valueOf(100));
        BigDecimal montoTotal = precioBase.multiply(toneladas);

        // Crear factura
        Factura factura = new Factura(cosechaId, montoTotal);
        factura.setCodigoQr(generarCodigoQR(factura.getFacturaId()));

        Factura facturaGuardada = facturaRepository.save(factura);

        // Actualizar estado en el microservicio Central
        try {
            centralServiceClient.actualizarEstadoCosecha(
                    cosechaId,
                    "FACTURADA",
                    facturaGuardada.getFacturaId()
            );
        } catch (Exception e) {
            // Log del error pero no fallar la transacción
            System.err.println("Error al actualizar estado en Central: " + e.getMessage());
        }

        return convertirADTO(facturaGuardada);
    }

    public FacturaDTO marcarComoPagada(UUID facturaId, String metodoPago) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.setPagado(true);
        factura.setMetodoPago(metodoPago);

        Factura actualizada = facturaRepository.save(factura);
        return convertirADTO(actualizada);
    }

    public List<FacturaDTO> obtenerPendientes() {
        return facturaRepository.findByPagado(false)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private String generarCodigoQR(UUID facturaId) {
        return "QR-" + facturaId.toString().substring(0, 8).toUpperCase();
    }

    private FacturaDTO convertirADTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setFacturaId(factura.getFacturaId());
        dto.setCosechaId(factura.getCosechaId());
        dto.setMontoTotal(factura.getMontoTotal());
        dto.setPagado(factura.getPagado());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setMetodoPago(factura.getMetodoPago());
        dto.setCodigoQr(factura.getCodigoQr());
        return dto;
    }
}