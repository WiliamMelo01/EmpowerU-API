package org.wiliammelo.empoweru.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GreetingsSQSPublisher implements MessagePublisher {

    private final JmsTemplate defJmsTemplate;

    @Value("${aws.sqs.greetings-queue-name}")
    private String greetingsQueueName;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void publish(Object message) {
        if (profile.equals("prod")) {
            defJmsTemplate.convertAndSend(greetingsQueueName, message);
        }
    }
}
