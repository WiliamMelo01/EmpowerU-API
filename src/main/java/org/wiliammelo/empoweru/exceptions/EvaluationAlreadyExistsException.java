package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class EvaluationAlreadyExistsException extends CustomException {

    public EvaluationAlreadyExistsException() {
        super("This section already has an evaluation activity.", HttpStatus.CONFLICT.value());
    }

}
