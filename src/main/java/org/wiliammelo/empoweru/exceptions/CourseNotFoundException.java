package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends CustomException{

    public CourseNotFoundException(){
        super("Course not found.", HttpStatus.NOT_FOUND.value());
    }

}
