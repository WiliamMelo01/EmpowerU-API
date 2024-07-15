package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.wiliammelo.empoweru.dtos.CustomResponse;
import org.wiliammelo.empoweru.dtos.ValidationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Centralized exception handling class across the whole application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    /**
     * Handles exceptions of type UnauthorizedException.
     * Maps the exception to an HTTP NOT FOUND response status.
     *
     * @param ex The caught UnauthorizedException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CustomResponse> handleVideoUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles exceptions of type SectionNotFoundException.
     * Maps the exception to an HTTP NOT FOUND response status.
     *
     * @param ex The caught SectionNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<CustomResponse> handleVideoSectionNotFoundException(SectionNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ValidationError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ValidationError(fieldName, errorMessage));
        });

        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
