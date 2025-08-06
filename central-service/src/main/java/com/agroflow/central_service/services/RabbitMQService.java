package com.agroflow.central_service.services;

import com.agroflow.central_service.dto.EventoCosechaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public RabbitMQService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void publicarEventoCosecha(EventoCosechaDTO evento) {
        try {
            String mensaje = objectMapper.writeValueAsString(evento);

            // Publicar a exchange "cosechas" con routing key "nueva"
            rabbitTemplate.convertAndSend("cosechas", "nueva", mensaje);

            logger.info("Evento de cosecha publicado: {}", evento.getPayload().getCosechaId());

        } catch (JsonProcessingException e) {
            logger.error("Error al serializar evento de cosecha", e);
            throw new RuntimeException("Error al publicar evento", e);
        }
    }
}