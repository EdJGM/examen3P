package ec.edu.espe.ms_inventario.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue inventarioQueue() {
        return QueueBuilder.durable("cola.inventario").build();
    }

    @Bean
    public Queue notificacionesQueue() {
        return QueueBuilder.durable("notificaciones_cola").build();
    }

    @Bean
    public TopicExchange cosechasExchange() {
        return new TopicExchange("cosechas.exchange");
    }

    // Configuración adicional si necesitas bindings específicos
}