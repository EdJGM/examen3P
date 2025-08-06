package ec.edu.espe.ms_inventario.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.ms_inventario.dto.EventoCosechaDTO;
import ec.edu.espe.ms_inventario.service.InsumoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CosechaListener {

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "cola.inventario")
    public void procesarEventoCosecha(String mensajeJson) {
        try {
            EventoCosechaDTO evento = objectMapper.readValue(mensajeJson, EventoCosechaDTO.class);

            System.out.println("📥 Evento recibido en Inventario: " + evento.getEventType());

            if ("nueva_cosecha".equals(evento.getEventType())) {
                insumoService.procesarCosecha(evento);
            }

        } catch (Exception e) {
            System.err.println("❌ Error procesando mensaje en Inventario: " + e.getMessage());
            e.printStackTrace();
        }
    }
}