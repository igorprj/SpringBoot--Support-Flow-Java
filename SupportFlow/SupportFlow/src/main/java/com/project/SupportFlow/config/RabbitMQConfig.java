package com.project.SupportFlow.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue ticketQueue() {
        return new Queue("supportflow.ticket.created", true);
    }

    @Bean
    public DirectExchange supportflowExchange() {
        return new DirectExchange("supportflow.ticket.exchange");
    }

    @Bean
    public Binding ticketBinding(Queue ticketQueue, DirectExchange supportflowExchange) {
        return BindingBuilder
                .bind(ticketQueue)
                .to(supportflowExchange)
                .with("supportflow.created");
    }
}
