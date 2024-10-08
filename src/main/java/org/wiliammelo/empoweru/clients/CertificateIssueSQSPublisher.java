package org.wiliammelo.empoweru.clients;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CertificateIssueSQSPublisher implements MessagePublisher {
    private final JmsTemplate defaultJmsTemplate;

    @Value("${aws.sqs.certificate-queue-name}")
    private String certificateQueue;

    @Override
    public void publish(Object message) {
        defaultJmsTemplate.convertAndSend(certificateQueue, new Gson().toJson(message));
    }
}
