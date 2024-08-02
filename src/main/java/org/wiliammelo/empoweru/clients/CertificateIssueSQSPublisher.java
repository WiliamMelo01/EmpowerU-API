package org.wiliammelo.empoweru.clients;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateIssueSQSPublisher implements MessagePublisher {
    private final JmsTemplate defaultJmsTemplate;

    @Value("${aws.sqs.certificate-queue-name}")
    private String certificateQueue;

    @Override
    public void publishJson(Object message) {
        defaultJmsTemplate.convertAndSend(certificateQueue, new Gson().toJson(message));
    }
}
