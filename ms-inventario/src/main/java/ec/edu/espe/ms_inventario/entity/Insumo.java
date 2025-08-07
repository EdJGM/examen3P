package ec.edu.espe.ms_inventario.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private BigDecimal stock = BigDecimal.ZERO;

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

    public boolean tieneStockSuficiente(BigDecimal cantidad) {
        return stock.compareTo(cantidad) >= 0;
    }

    public void descontarStock(BigDecimal cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            throw new RuntimeException(
                    String.format("Stock insuficiente para %s. Disponible: %s, Requerido: %s",
                            nombreInsumo, stock, cantidad)
            );
        }
        stock = stock.subtract(cantidad);
    }
    public boolean estaEnStockMinimo() {
        return stock.compareTo(BigDecimal.valueOf(stockMinimo)) <= 0;
    }
}