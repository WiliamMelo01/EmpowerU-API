package org.wiliammelo.empoweru.dtos;

import lombok.Getter;

import java.sql.Timestamp;

/**
 * Entity class representing a custom response.
 */
@Getter
public class CustomResponse {

    private final Integer status;
    private final String message;
    private final Timestamp timestamp;

    public CustomResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
