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
        return QueueBuilder.durable("cola_inventario").build();
    }

    @Bean
    public Queue notificacionesQueue() {
        return QueueBuilder.durable("cola_notificaciones").build();
    }

    @Bean
    public TopicExchange cosechasExchange() {
        return new TopicExchange("cosechas");  // Cambiado para coincidir con los otros servicios
    }

    @Bean
    public Binding bindingInventario() {
        return BindingBuilder
                .bind(inventarioQueue())
                .to(cosechasExchange())
                .with("nueva");
    }

    @Bean
    public Binding bindingNotificaciones() {
        return BindingBuilder
                .bind(notificacionesQueue())
                .to(cosechasExchange())
                .with("factura.generada");
    }
}