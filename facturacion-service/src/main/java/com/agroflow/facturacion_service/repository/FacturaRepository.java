package com.agroflow.facturacion_service.repository;

import com.agroflow.facturacion_service.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, UUID> {

    Optional<Factura> findByCosechaId(UUID cosechaId);
    List<Factura> findByPagado(Boolean pagado);
}