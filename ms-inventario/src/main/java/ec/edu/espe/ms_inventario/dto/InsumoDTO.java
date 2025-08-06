package ec.edu.espe.ms_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoDTO {
    private String nombreInsumo;
    private Integer stock;
    private String unidadMedida;
    private String categoria;
    private Double precioUnitario;
    private Integer stockMinimo;
}