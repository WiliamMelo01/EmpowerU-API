package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(){
        super("User not found", HttpStatus.NOT_FOUND.value());
    }

}
