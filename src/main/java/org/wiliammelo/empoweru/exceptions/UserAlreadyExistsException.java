package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {

    public UserAlreadyExistsException() {
        super("User already exists.", HttpStatus.CONFLICT.value());
    }
}
