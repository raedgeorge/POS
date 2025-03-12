package com.atech.pos.exceptions.handlers;

import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalDomainExceptionHandler {

    @ExceptionHandler(value = ResourceExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceExistsException(Exception exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ResourceExistsException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(Exception exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("Entity"));
        problemDetail.setInstance(URI.create("ResourceNotFoundException"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
