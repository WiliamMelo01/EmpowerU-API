package org.wiliammelo.empoweru.email;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Service for sending emails via AMQP (Advanced Message Queuing Protocol).
 * Utilizes RabbitMQ as the message broker.
 */
@Service
public class EmailService {

    private final AmqpTemplate amqpTemplate;

    private final String exchange;

    private final String routingkey;

    /**
     * Constructs an EmailService with the necessary AMQP template and RabbitMQ configurations.
     *
     * @param amqpTemplate the AMQP template for message sending operations
     * @param exchange     the name of the RabbitMQ exchange to use
     * @param routingkey   the routing key for directing the message to the correct queue
     */
    public EmailService(AmqpTemplate amqpTemplate, @Value("${rabbitmq.exchange}") String exchange, @Value("${rabbitmq.routingkey}") String routingkey) {
        this.amqpTemplate = amqpTemplate;
        this.exchange = exchange;
        this.routingkey = routingkey;
    }

    /**
     * Sends an email message using the configured AMQP template, exchange, and routing key.
     *
     * @param email the email content to send
     */
    public void send(String email) {
        amqpTemplate.convertAndSend(exchange, routingkey, email);
    }

}
