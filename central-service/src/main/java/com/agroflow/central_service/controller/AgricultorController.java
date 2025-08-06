package com.agroflow.central_service.controller;

import com.agroflow.central_service.dto.AgricultorDTO;
import com.agroflow.central_service.services.AgricultorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agricultores")
public class AgricultorController {

    @Autowired
    private AgricultorService agricultorService;

    @GetMapping
    public ResponseEntity<List<AgricultorDTO>> obtenerTodos() {
        List<AgricultorDTO> agricultores = agricultorService.obtenerTodos();
        return ResponseEntity.ok(agricultores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgricultorDTO> obtenerPorId(@PathVariable UUID id) {
        return agricultorService.obtenerPorId(id)
                .map(agricultor -> ResponseEntity.ok(agricultor))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgricultorDTO> crear(@Valid @RequestBody AgricultorDTO agricultorDTO) {
        try {
            AgricultorDTO nuevoAgricultor = agricultorService.crear(agricultorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAgricultor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgricultorDTO> actualizar(@PathVariable UUID id,
                                                    @Valid @RequestBody AgricultorDTO agricultorDTO) {
        try {
            AgricultorDTO agricultorActualizado = agricultorService.actualizar(id, agricultorDTO);
            return ResponseEntity.ok(agricultorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            agricultorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}