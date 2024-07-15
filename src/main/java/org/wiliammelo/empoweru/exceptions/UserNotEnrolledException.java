package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotEnrolledException extends CustomException {

    public UserNotEnrolledException() {
        super("User is not enrolled in the course.", HttpStatus.BAD_REQUEST.value());
    }

}
