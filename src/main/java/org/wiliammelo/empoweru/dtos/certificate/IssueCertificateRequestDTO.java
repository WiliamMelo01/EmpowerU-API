package org.wiliammelo.empoweru.dtos.certificate;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class IssueCertificateRequestDTO implements Serializable {

    private String userName;
    private String courseTitle;
    private String email;

}
