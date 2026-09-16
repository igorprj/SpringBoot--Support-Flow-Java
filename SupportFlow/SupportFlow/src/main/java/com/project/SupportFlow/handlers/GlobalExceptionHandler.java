package com.project.SupportFlow.handlers;

import com.project.SupportFlow.exceptions.InvalidTicketTransiction;
import com.project.SupportFlow.exceptions.TicketNotFoundException;
import com.project.SupportFlow.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException ex,  HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.name(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorResponse.toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleException(TicketNotFoundException ex,  HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.name(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorResponse.toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTicketTransiction.class)
    public ResponseEntity<String> handleException(InvalidTicketTransiction ex,  HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.name(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse.toString(), HttpStatus.BAD_REQUEST);
    }
}
