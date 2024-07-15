package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class SectionNotFoundException extends CustomException {

    public SectionNotFoundException() {
        super("Section not found.", HttpStatus.NOT_FOUND.value());
    }

}
