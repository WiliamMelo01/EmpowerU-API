package org.wiliammelo.empoweru.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CertificateRabbitMQConfig {

    // Configuration for the certificate issuance queue
    @Bean
    public Queue certificateQueue() {
        return new Queue("certificateQueue", true);
    }

    // Configuration for the certificate issuance exchange
    @Bean
    public TopicExchange certificateExchange() {
        return new TopicExchange("certificateExchange");
    }

    // Binding to connect the certificate queue to the certificate exchange
    @Bean
    public Binding certificateBinding(Queue certificateQueue, TopicExchange certificateExchange) {
        return BindingBuilder.bind(certificateQueue).to(certificateExchange).with("certificateRoutingKey");
    }
}
