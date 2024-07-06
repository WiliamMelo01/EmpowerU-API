package org.wiliammelo.empoweru.exceptions;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }

}
