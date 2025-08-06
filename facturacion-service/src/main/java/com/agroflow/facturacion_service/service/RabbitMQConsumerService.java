package com.agroflow.facturacion_service.service;


import com.agroflow.facturacion_service.dto.EventoCosechaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    @Autowired
    private FacturaService facturaService;

    private final ObjectMapper objectMapper;

    public RabbitMQConsumerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @RabbitListener(queues = "cola_facturacion")
    public void procesarEventoCosecha(String mensaje) {
        try {
            logger.info("Mensaje recibido en cola_facturacion: {}", mensaje);

            EventoCosechaDTO evento = objectMapper.readValue(mensaje, EventoCosechaDTO.class);

            if ("nueva_cosecha".equals(evento.getEventType())) {
                EventoCosechaDTO.PayloadCosecha payload = evento.getPayload();

                logger.info("Procesando nueva cosecha: {} - {} toneladas de {}",
                        payload.getCosechaId(),
                        payload.getToneladas(),
                        payload.getProducto());

                // Procesar la facturación
                facturaService.procesarCosecha(
                        payload.getCosechaId(),
                        payload.getProducto(),
                        payload.getToneladas()
                );

                logger.info("Factura generada exitosamente para cosecha: {}", payload.getCosechaId());

            } else {
                logger.warn("Tipo de evento no reconocido: {}", evento.getEventType());
            }

        } catch (Exception e) {
            logger.error("Error al procesar mensaje de cosecha: {}", e.getMessage(), e);
        }
    }
}