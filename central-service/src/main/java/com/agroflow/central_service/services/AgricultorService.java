package com.agroflow.central_service.services;

import com.agroflow.central_service.dto.AgricultorDTO;
import com.agroflow.central_service.entity.Agricultor;
import com.agroflow.central_service.repository.AgricultorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgricultorService {

    @Autowired
    private AgricultorRepository agricultorRepository;

    public List<AgricultorDTO> obtenerTodos() {
        return agricultorRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<AgricultorDTO> obtenerPorId(UUID id) {
        return agricultorRepository.findById(id)
                .map(this::convertirADTO);
    }

    public AgricultorDTO crear(AgricultorDTO agricultorDTO) {
        if (agricultorRepository.existsByCorreo(agricultorDTO.getCorreo())) {
            throw new RuntimeException("Ya existe un agricultor con este correo");
        }

        Agricultor agricultor = convertirAEntidad(agricultorDTO);
        Agricultor guardado = agricultorRepository.save(agricultor);
        return convertirADTO(guardado);
    }

    public AgricultorDTO actualizar(UUID id, AgricultorDTO agricultorDTO) {
        Agricultor agricultor = agricultorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        // Verificar que el correo no esté en uso por otro agricultor
        if (!agricultor.getCorreo().equals(agricultorDTO.getCorreo()) &&
                agricultorRepository.existsByCorreo(agricultorDTO.getCorreo())) {
            throw new RuntimeException("Ya existe un agricultor con este correo");
        }

        agricultor.setNombre(agricultorDTO.getNombre());
        agricultor.setFinca(agricultorDTO.getFinca());
        agricultor.setUbicacion(agricultorDTO.getUbicacion());
        agricultor.setCorreo(agricultorDTO.getCorreo());

        Agricultor actualizado = agricultorRepository.save(agricultor);
        return convertirADTO(actualizado);
    }

    public void eliminar(UUID id) {
        if (!agricultorRepository.existsById(id)) {
            throw new RuntimeException("Agricultor no encontrado");
        }
        agricultorRepository.deleteById(id);
    }

    private AgricultorDTO convertirADTO(Agricultor agricultor) {
        AgricultorDTO dto = new AgricultorDTO();
        dto.setAgricultorId(agricultor.getAgricultorId());
        dto.setNombre(agricultor.getNombre());
        dto.setFinca(agricultor.getFinca());
        dto.setUbicacion(agricultor.getUbicacion());
        dto.setCorreo(agricultor.getCorreo());
        dto.setFechaRegistro(agricultor.getFechaRegistro());
        return dto;
    }

    private Agricultor convertirAEntidad(AgricultorDTO dto) {
        Agricultor agricultor = new Agricultor();
        agricultor.setNombre(dto.getNombre());
        agricultor.setFinca(dto.getFinca());
        agricultor.setUbicacion(dto.getUbicacion());
        agricultor.setCorreo(dto.getCorreo());
        return agricultor;
    }
}