package org.wiliammelo.empoweru.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CustomResponse {

    private Integer status;
    private String message;
    private Timestamp timestamp;

    public CustomResponse(String message, Integer status){
        this.message = message;
        this.status = status;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
