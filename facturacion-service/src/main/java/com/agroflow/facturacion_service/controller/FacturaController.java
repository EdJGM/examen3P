package com.agroflow.facturacion_service.controller;


import com.agroflow.facturacion_service.dto.FacturaDTO;
import com.agroflow.facturacion_service.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> obtenerTodas() {
        List<FacturaDTO> facturas = facturaService.obtenerTodas();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable UUID id) {
        return facturaService.obtenerPorId(id)
                .map(factura -> ResponseEntity.ok(factura))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cosecha/{cosechaId}")
    public ResponseEntity<FacturaDTO> obtenerPorCosechaId(@PathVariable UUID cosechaId) {
        return facturaService.obtenerPorCosechaId(cosechaId)
                .map(factura -> ResponseEntity.ok(factura))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<FacturaDTO>> obtenerPendientes() {
        List<FacturaDTO> facturasPendientes = facturaService.obtenerPendientes();
        return ResponseEntity.ok(facturasPendientes);
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<FacturaDTO> marcarComoPagada(@PathVariable UUID id,
                                                       @RequestBody Map<String, String> pagoRequest) {
        try {
            String metodoPago = pagoRequest.get("metodo_pago");
            FacturaDTO facturaPagada = facturaService.marcarComoPagada(id, metodoPago);
            return ResponseEntity.ok(facturaPagada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para pruebas manuales (crear factura directamente)
    @PostMapping("/manual")
    public ResponseEntity<FacturaDTO> crearFacturaManual(@RequestBody Map<String, Object> request) {
        try {
            UUID cosechaId = UUID.fromString((String) request.get("cosecha_id"));
            String producto = (String) request.get("producto");
            Double toneladas = Double.valueOf(request.get("toneladas").toString());

            FacturaDTO nuevaFactura = facturaService.procesarCosecha(
                    cosechaId,
                    producto,
                    java.math.BigDecimal.valueOf(toneladas)
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
