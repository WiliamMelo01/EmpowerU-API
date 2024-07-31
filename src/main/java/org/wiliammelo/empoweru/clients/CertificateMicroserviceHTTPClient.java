package org.wiliammelo.empoweru.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.wiliammelo.empoweru.dtos.certificate.IssueCertificateRequestDTO;

@FeignClient(name = "certificate-microservice", url = "${certificate-microservice.url}")
public interface CertificateMicroserviceHTTPClient {

    @PostMapping("/api/certificate")
    void generateCertificate(@RequestBody IssueCertificateRequestDTO issueCertificateRequestDTO);

}
