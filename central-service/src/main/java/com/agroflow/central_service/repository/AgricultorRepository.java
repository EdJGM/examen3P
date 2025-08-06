package com.agroflow.central_service.repository;

import com.agroflow.central_service.entity.Agricultor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgricultorRepository extends JpaRepository<Agricultor, UUID> {
    Optional<Agricultor> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}