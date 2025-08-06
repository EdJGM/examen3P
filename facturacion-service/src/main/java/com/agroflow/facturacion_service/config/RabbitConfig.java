package com.agroflow.facturacion_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Exchange principal
    @Bean
    public TopicExchange cosechasExchange() {
        return new TopicExchange("cosechas");
    }

    // Cola para facturación
    @Bean
    public Queue colaFacturacion() {
        return QueueBuilder.durable("cola_facturacion").build();
    }

    // Binding para recibir eventos de nueva cosecha
    @Bean
    public Binding bindingFacturacion() {
        return BindingBuilder
                .bind(colaFacturacion())
                .to(cosechasExchange())
                .with("nueva");
    }
}