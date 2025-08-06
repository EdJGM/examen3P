package com.agroflow.facturacion_service.service;

import com.agroflow.facturacion_service.dto.EstadoCosechaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class CentralServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CentralServiceClient.class);

    @Value("${central-service.url:http://localhost:8080}")
    private String centralServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void actualizarEstadoCosecha(UUID cosechaId, String estado, UUID facturaId) {
        try {
            String url = centralServiceUrl + "/api/cosechas/" + cosechaId + "/estado";

            EstadoCosechaRequest request = new EstadoCosechaRequest(estado, facturaId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EstadoCosechaRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Estado de cosecha actualizado exitosamente: {} -> {}", cosechaId, estado);
            } else {
                logger.warn("Error al actualizar estado de cosecha: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            logger.error("Error al comunicarse con el servicio Central: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar estado en Central", e);
        }
    }
}