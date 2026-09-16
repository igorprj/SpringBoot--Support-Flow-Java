package com.project.SupportFlow.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private String message;

    private int status;
    private String path;

    public ErrorResponse(String message, String name, String requestURI, int value) {
        this.timestamp = LocalDateTime.now();
        this.error = name;
        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.path = requestURI;
    }
}
