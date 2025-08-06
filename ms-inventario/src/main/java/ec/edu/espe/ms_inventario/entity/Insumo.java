package ec.edu.espe.ms_inventario.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "insumo_id")
    private UUID insumoId;

    @Column(name = "nombre_insumo", unique = true, nullable = false, length = 100)
    private String nombreInsumo;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "unidad_medida", length = 10)
    private String unidadMedida = "kg";

    @Column(nullable = false, length = 30)
    private String categoria;

    @Column(name = "precio_unitario")
    private Double precioUnitario = 0.0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 10;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }

    public boolean tieneStockSuficiente(Integer cantidad) {
        return stock >= cantidad;
    }

    public void descontarStock(Integer cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            throw new RuntimeException(
                    String.format("Stock insuficiente para %s. Disponible: %d, Requerido: %d",
                            nombreInsumo, stock, cantidad)
            );
        }
        stock -= cantidad;
    }

    public boolean estaEnStockMinimo() {
        return stock <= stockMinimo;
    }
}