package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class CanNotIssueCertificateException extends CustomException {

    public CanNotIssueCertificateException() {
        super("To issue a certificate, the student must complete all activities with a grade higher than 70%.", HttpStatus.BAD_REQUEST.value());
    }

}
