package com.atech.pos.exceptions.handlers;

import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Objects;

@RestControllerAdvice
public class GlobalDomainExceptionHandler {

    @ExceptionHandler(value = ResourceExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceExistsException(ResourceExistsException exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ResourceExistsException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception){

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

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ValidationException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        String errorMessage = exception.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce(Objects::toString)
                .orElse("Validation exception. Unknown error");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("MethodArgumentNotValidException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
