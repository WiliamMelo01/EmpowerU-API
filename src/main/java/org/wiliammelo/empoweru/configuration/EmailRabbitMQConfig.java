package org.wiliammelo.empoweru.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailRabbitMQConfig {

    // Configuration for the email queue
    @Bean
    public Queue queue() {
        return new Queue("emailQueue", true);
    }

    // Configuration for the email exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("emailExchange");
    }

    // Binding to connect the email queue to the email exchange
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("emailRoutingKey");
    }
}
