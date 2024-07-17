package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class EvaluationActivityNotFoundException extends CustomException {

    public EvaluationActivityNotFoundException() {
        super("Evaluation not found", HttpStatus.NOT_FOUND.value());
    }

}
