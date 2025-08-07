package ec.edu.espe.ms_inventario.controller;


import ec.edu.espe.ms_inventario.dto.EventoCosechaDTO;
import ec.edu.espe.ms_inventario.dto.InsumoDTO;
import ec.edu.espe.ms_inventario.dto.ResponseDTO;
import ec.edu.espe.ms_inventario.entity.Insumo;
import ec.edu.espe.ms_inventario.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventario/insumos")
@CrossOrigin(origins = "*")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @GetMapping
    public ResponseEntity<ResponseDTO> listarInsumos() {
        List<Insumo> insumos = insumoService.listarTodos();
        return ResponseEntity.ok(new ResponseDTO("Lista de insumos", insumos));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ResponseDTO> listarDisponibles() {
        List<Insumo> insumos = insumoService.listarDisponibles();
        return ResponseEntity.ok(new ResponseDTO("Insumos disponibles", insumos));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<ResponseDTO> listarStockBajo() {
        List<Insumo> insumos = insumoService.listarConStockBajo();
        return ResponseEntity.ok(new ResponseDTO("Insumos con stock bajo", insumos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable UUID id) {
        Insumo insumo = insumoService.buscarPorId(id);
        return ResponseEntity.ok(new ResponseDTO("Insumo encontrado", insumo));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> crearInsumo(@RequestBody InsumoDTO dto) {
        try {
            Insumo insumo = insumoService.crear(dto);
            return ResponseEntity.ok(new ResponseDTO("Insumo creado correctamente", insumo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO("Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> actualizarInsumo(
            @PathVariable UUID id, @RequestBody InsumoDTO dto) {
        Insumo insumo = insumoService.actualizar(id, dto);
        return ResponseEntity.ok(new ResponseDTO("Insumo actualizado correctamente", insumo));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ResponseDTO> actualizarStock(
            @PathVariable UUID id, @RequestParam BigDecimal nuevoStock) {
        Insumo insumo = insumoService.actualizarStock(id, nuevoStock);
        return ResponseEntity.ok(new ResponseDTO("Stock actualizado correctamente", insumo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarInsumo(@PathVariable UUID id) {
        insumoService.eliminar(id);
        return ResponseEntity.ok(new ResponseDTO("Insumo eliminado correctamente", null));
    }

    @PostMapping("/procesar-cosecha")
    public ResponseEntity<ResponseDTO> procesarCosechaManual(@RequestBody Map<String, Object> request) {
        try {
            UUID cosechaId = UUID.fromString((String) request.get("cosecha_id"));
            String producto = (String) request.get("producto");
            BigDecimal toneladas = new BigDecimal(request.get("toneladas").toString());

            // Crear evento local
            EventoCosechaDTO evento = new EventoCosechaDTO();
            evento.setCosechaId(cosechaId);
            evento.setProducto(producto);
            evento.setToneladas(toneladas);
            evento.setEventType("nueva_cosecha");
            evento.setTimestamp(LocalDateTime.now());

            insumoService.procesarCosecha(evento);

            return ResponseEntity.ok(new ResponseDTO("Cosecha procesada correctamente en inventario", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO("Error: " + e.getMessage(), null));
        }
    }
}