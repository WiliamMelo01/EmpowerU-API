package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wiliammelo.empoweru.models.CustomResponse;

@ControllerAdvice
public class GlobalExceptionHanlder {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<CustomResponse> handleCourseNotFoundException(CourseNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<CustomResponse> handleVideoNotFoundException(VideoNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.NOT_FOUND);
    }

}
