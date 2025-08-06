package ec.edu.espe.ms_inventario.repository;

import ec.edu.espe.ms_inventario.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, UUID> {

    Optional<Insumo> findByNombreInsumo(String nombreInsumo);

    List<Insumo> findByCategoria(String categoria);

    @Query("SELECT i FROM Insumo i WHERE i.stock <= i.stockMinimo")
    List<Insumo> findInsumosConStockBajo();

    @Query("SELECT i FROM Insumo i WHERE i.stock > 0 ORDER BY i.nombreInsumo")
    List<Insumo> findInsumosDisponibles();
}