package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wiliammelo.empoweru.dtos.CustomResponse;

/**
 * Centralized exception handling class across the whole application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type UserAlreadyExistsException.
     * Maps the exception to an HTTP CONFLICT response status.
     *
     * @param ex The caught UserAlreadyExistsException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions of type UserNotFoundException.
     * Maps the exception to an HTTP CONFLICT response status.
     *
     * @param ex The caught UserNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions of type CourseNotFoundException.
     * Maps the exception to an HTTP NOT FOUND response status.
     *
     * @param ex The caught CourseNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<CustomResponse> handleCourseNotFoundException(CourseNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions of type VideoNotFoundException.
     * Maps the exception to an HTTP NOT FOUND response status.
     *
     * @param ex The caught VideoNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<CustomResponse> handleVideoNotFoundException(VideoNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.NOT_FOUND);
    }

}
