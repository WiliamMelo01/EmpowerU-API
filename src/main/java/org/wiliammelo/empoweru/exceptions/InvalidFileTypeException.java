package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidFileTypeException extends CustomException {

    public InvalidFileTypeException(List<String> allowedTypes) {
        super("Invalid file type. Allowed types are: " + allowedTypes, HttpStatus.BAD_REQUEST.value());
    }

}
