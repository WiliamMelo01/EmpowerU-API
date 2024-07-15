package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyEnrolledException extends CustomException {

    public UserAlreadyEnrolledException() {
        super("User is already enrolled in this course.", HttpStatus.CONFLICT.value());
    }


}
