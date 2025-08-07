package ec.edu.espe.ms_inventario.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoCosechaDTO {
    private UUID eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private UUID cosechaId;
    private String producto;
    private BigDecimal toneladas;
}