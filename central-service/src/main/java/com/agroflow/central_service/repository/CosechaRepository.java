package com.agroflow.central_service.repository;

import com.agroflow.central_service.entity.Cosecha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CosechaRepository extends JpaRepository<Cosecha, UUID> {

    @Query("SELECT c FROM Cosecha c WHERE c.agricultor.agricultorId = :agricultorId")
    List<Cosecha> findByAgricultorId(@Param("agricultorId") UUID agricultorId);

    List<Cosecha> findByEstado(String estado);
}