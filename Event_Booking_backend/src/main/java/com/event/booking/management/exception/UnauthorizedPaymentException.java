package com.event.booking.management.exception;

public class UnauthorizedPaymentException extends RuntimeException {
    public UnauthorizedPaymentException(String message) {
        super(message);
    }
}
