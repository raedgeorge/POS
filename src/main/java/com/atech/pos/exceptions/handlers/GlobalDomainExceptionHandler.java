package com.atech.pos.exceptions.handlers;

import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalDomainExceptionHandler {

    @ExceptionHandler(value = ResourceExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceExistsException(ResourceExistsException exception){

        logErrorMessage(ResourceExistsException.class.getName(), exception.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ResourceExistsException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception){

        logErrorMessage(ResourceNotFoundException.class.getName(), exception.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ResourceNotFoundException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler({
            ValidationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ProblemDetail> handleValidationException(Exception exception){

        logErrorMessage(exception.getClass().getName(), exception.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ValidationException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        StringBuilder errorBuilder = new StringBuilder();

        exception.getFieldErrors().forEach(fieldError ->
            errorBuilder.append(fieldError.getField())
                  .append(": ")
                  .append(fieldError.getDefaultMessage())
                  .append(". "));

        String error = errorBuilder.toString().trim();

        logErrorMessage(MethodArgumentNotValidException.class.getName(), error);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error);
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("MethodArgumentNotValidException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private void logErrorMessage(String exceptionalClass, String message){
        log.error("Exception: {}, message: {}", exceptionalClass, message);
    }
}
