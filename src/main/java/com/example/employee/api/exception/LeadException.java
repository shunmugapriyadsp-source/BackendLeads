package com.example.employee.api.exception;

public class LeadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LeadException(String message) {
        super(message);
    }
}