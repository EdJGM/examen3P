package ec.edu.espe.ms_inventario.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.ms_inventario.dto.NotificacionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void enviarNotificacion(String mensaje, String tipo) {
        try {
            NotificacionDTO dto = new NotificacionDTO(mensaje, tipo);
            String json = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend("cola_notificaciones", json);
            System.out.println("📧 Notificación enviada: " + mensaje);
        } catch (Exception e) {
            System.err.println("❌ Error enviando notificación: " + e.getMessage());
        }
    }
}