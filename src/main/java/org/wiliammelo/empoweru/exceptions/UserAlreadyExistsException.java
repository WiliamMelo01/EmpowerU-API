package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {

    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
