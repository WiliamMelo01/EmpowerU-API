package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
     * Maps the exception to an HTTP UNAUTHORIZED response status.
     *
     * @param ex The caught UnauthorizedException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CustomResponse> handleUnauthorizedException(UnauthorizedException ex) {
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
    public ResponseEntity<CustomResponse> handleSectionNotFoundException(SectionNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions of type UserAlreadyEnrolledException.
     * Maps the exception to an HTTP CONFLICT response status.
     *
     * @param ex The caught SectionNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(UserAlreadyEnrolledException.class)
    public ResponseEntity<CustomResponse> handleUserAlreadyEnrolledException(UserAlreadyEnrolledException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions of type UserNotEnrolledException.
     * Maps the exception to an HTTP BAD_REQUEST response status.
     *
     * @param ex The caught SectionNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(UserNotEnrolledException.class)
    public ResponseEntity<CustomResponse> handleUserUserNotEnrolledException(UserNotEnrolledException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type EvaluationAlreadyExistsException.
     * Maps the exception to an HTTP CONFLICT response status.
     *
     * @param ex The caught EvaluationAlreadyExistsException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(EvaluationAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleEvaluationAlreadyExistsException(EvaluationAlreadyExistsException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions of type EvaluationActivityNotFoundException.
     * Maps the exception to an HTTP NOT_FOUND response status.
     *
     * @param ex The caught EvaluationActivityNotFoundException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(EvaluationActivityNotFoundException.class)
    public ResponseEntity<CustomResponse> handleEvaluationActivityNotFoundException(EvaluationActivityNotFoundException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions of type CanNotIssueCertificateException.
     * Maps the exception to an HTTP BAD_REQUEST response status.
     *
     * @param ex The caught CanNotIssueCertificateException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(CanNotIssueCertificateException.class)
    public ResponseEntity<CustomResponse> handleCanNotIssueCertificateException(CanNotIssueCertificateException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles exceptions of type BadCredentialsException.
     * Maps the exception to an HTTP BAD_REQUEST response status.
     *
     * @param ex The caught BadCredentialsException.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type Exception.
     * Maps the exception to an HTTP v response status.
     *
     * @param ex The caught Exception.
     * @return A ResponseEntity containing a CustomResponse with the exception's message and error details, and the HTTP status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(new CustomResponse(ex.getMessage(), ex.getError()), HttpStatusCode.valueOf(ex.getError()));
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
