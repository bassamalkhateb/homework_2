package com.example.ELerning.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

   
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<Object> handleResourceNotFound(
        ResourceNotFoundException ex, 
        WebRequest request) {
    
    ErrorDetails errorDetails = new ErrorDetails(
        LocalDateTime.now(),
        ex.getMessage(),
        request.getDescription(false)
    );
    
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
}
  
}