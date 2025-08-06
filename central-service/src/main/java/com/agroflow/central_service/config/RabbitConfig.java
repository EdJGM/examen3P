package com.agroflow.central_service.config;

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

    // Colas
    @Bean
    public Queue colaInventario() {
        return QueueBuilder.durable("cola_inventario").build();
    }

    @Bean
    public Queue colaFacturacion() {
        return QueueBuilder.durable("cola_facturacion").build();
    }

    @Bean
    public Queue colaNotificaciones() {
        return QueueBuilder.durable("cola_notificaciones").build();
    }

    // Bindings
    @Bean
    public Binding bindingInventario() {
        return BindingBuilder
                .bind(colaInventario())
                .to(cosechasExchange())
                .with("nueva");
    }

    @Bean
    public Binding bindingFacturacion() {
        return BindingBuilder
                .bind(colaFacturacion())
                .to(cosechasExchange())
                .with("nueva");
    }

    @Bean
    public Binding bindingNotificaciones() {
        return BindingBuilder
                .bind(colaNotificaciones())
                .to(cosechasExchange())
                .with("factura.generada");
    }
}