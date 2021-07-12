package com.invoiceservice.exceptionHandling;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private HttpStatus status;

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.status = httpStatus;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
