package com.agroflow.central_service.controller;

import com.agroflow.central_service.dto.CosechaDTO;
import com.agroflow.central_service.services.CosechaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cosechas")
public class CosechaController {

    @Autowired
    private CosechaService cosechaService;

    @GetMapping
    public ResponseEntity<List<CosechaDTO>> obtenerTodas() {
        List<CosechaDTO> cosechas = cosechaService.obtenerTodas();
        return ResponseEntity.ok(cosechas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CosechaDTO> obtenerPorId(@PathVariable UUID id) {
        return cosechaService.obtenerPorId(id)
                .map(cosecha -> ResponseEntity.ok(cosecha))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CosechaDTO> crear(@Valid @RequestBody CosechaDTO cosechaDTO) {
        try {
            CosechaDTO nuevaCosecha = cosechaService.crear(cosechaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCosecha);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<CosechaDTO> actualizarEstado(@PathVariable UUID id,
                                                       @RequestBody Map<String, Object> estadoRequest) {
        try {
            String estado = (String) estadoRequest.get("estado");
            Object facturaIdObj = estadoRequest.get("factura_id");
            if (facturaIdObj == null) {
                facturaIdObj = estadoRequest.get("facturaId");
            }

            UUID facturaId = facturaIdObj != null ? UUID.fromString(facturaIdObj.toString()) : null;

            CosechaDTO cosechaActualizada = cosechaService.actualizarEstado(id, estado, facturaId);
            return ResponseEntity.ok(cosechaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            cosechaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}