package com.agroflow.central_service.services;

import com.agroflow.central_service.dto.CosechaDTO;
import com.agroflow.central_service.dto.EventoCosechaDTO;
import com.agroflow.central_service.entity.Agricultor;
import com.agroflow.central_service.entity.Cosecha;
import com.agroflow.central_service.repository.AgricultorRepository;
import com.agroflow.central_service.repository.CosechaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CosechaService {

    @Autowired
    private CosechaRepository cosechaRepository;

    @Autowired
    private AgricultorRepository agricultorRepository;

    @Autowired
    private RabbitMQService rabbitMQService;

    public List<CosechaDTO> obtenerTodas() {
        return cosechaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<CosechaDTO> obtenerPorId(UUID id) {
        return cosechaRepository.findById(id)
                .map(this::convertirADTO);
    }

    public CosechaDTO crear(CosechaDTO cosechaDTO) {
        // Validar que existe el agricultor
        Agricultor agricultor = agricultorRepository.findById(cosechaDTO.getAgricultorId())
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        // Crear la cosecha
        Cosecha cosecha = new Cosecha();
        cosecha.setAgricultor(agricultor);
        cosecha.setProducto(cosechaDTO.getProducto());
        cosecha.setToneladas(cosechaDTO.getToneladas());
        cosecha.setEstado("REGISTRADA");

        Cosecha guardada = cosechaRepository.save(cosecha);

        // Publicar evento en RabbitMQ
        EventoCosechaDTO evento = new EventoCosechaDTO(
                guardada.getCosechaId(),
                guardada.getProducto(),
                guardada.getToneladas()
        );

        rabbitMQService.publicarEventoCosecha(evento);

        return convertirADTO(guardada);
    }

    public CosechaDTO actualizarEstado(UUID cosechaId, String nuevoEstado, UUID facturaId) {
        Cosecha cosecha = cosechaRepository.findById(cosechaId)
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada"));

        cosecha.setEstado(nuevoEstado);
        if (facturaId != null) {
            cosecha.setFacturaId(facturaId);
        }

        Cosecha actualizada = cosechaRepository.save(cosecha);
        return convertirADTO(actualizada);
    }

    public void eliminar(UUID id) {
        if (!cosechaRepository.existsById(id)) {
            throw new RuntimeException("Cosecha no encontrada");
        }
        cosechaRepository.deleteById(id);
    }

    private CosechaDTO convertirADTO(Cosecha cosecha) {
        CosechaDTO dto = new CosechaDTO();
        dto.setCosechaId(cosecha.getCosechaId());
        dto.setAgricultorId(cosecha.getAgricultor().getAgricultorId());
        dto.setProducto(cosecha.getProducto());
        dto.setToneladas(cosecha.getToneladas());
        dto.setEstado(cosecha.getEstado());
        dto.setCreadoEn(cosecha.getCreadoEn());
        dto.setFacturaId(cosecha.getFacturaId());
        return dto;
    }
}