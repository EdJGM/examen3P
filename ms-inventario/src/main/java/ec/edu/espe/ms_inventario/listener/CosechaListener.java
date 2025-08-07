package ec.edu.espe.ms_inventario.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ec.edu.espe.ms_inventario.dto.EventoCosechaDTO;
import ec.edu.espe.ms_inventario.service.InsumoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CosechaListener {

    @Autowired
    private InsumoService insumoService;

    private final ObjectMapper objectMapper;

    public CosechaListener() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @RabbitListener(queues = "cola_inventario")
    public void procesarEventoCosecha(String mensajeJson) {
        try {
            // Deserializar el evento completo del Central
            ObjectNode eventoNode = (ObjectNode) objectMapper.readTree(mensajeJson);
            ObjectNode payload = (ObjectNode) eventoNode.get("payload");

            // Extraer datos del payload
            UUID cosechaId = UUID.fromString(payload.get("cosechaId").asText());
            String producto = payload.get("producto").asText();
            BigDecimal toneladas = new BigDecimal(payload.get("toneladas").asText());

            System.out.println("🌾 Procesando cosecha: " + producto + " - " + toneladas + "t");

            // Crear evento local para procesar
            EventoCosechaDTO eventoLocal = new EventoCosechaDTO();
            eventoLocal.setCosechaId(cosechaId);
            eventoLocal.setProducto(producto);
            eventoLocal.setToneladas(toneladas);
            eventoLocal.setEventType("nueva_cosecha");
            eventoLocal.setTimestamp(LocalDateTime.now());

            insumoService.procesarCosecha(eventoLocal);

        } catch (Exception e) {
            System.err.println("❌ Error procesando mensaje en Inventario: " + e.getMessage());
        }
    }
}