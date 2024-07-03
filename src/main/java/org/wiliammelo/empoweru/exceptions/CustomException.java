package org.wiliammelo.empoweru.exceptions;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CustomException extends Exception {

    private final Integer error;
    private final String message;
    private final Timestamp timestamp;

    public CustomException(String message, Integer error) {
        super(message);
        this.message = message;
        this.error = error;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
