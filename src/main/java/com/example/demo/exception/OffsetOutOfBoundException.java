package com.example.demo.exception;

public class OffsetOutOfBoundException extends RuntimeException {

    public OffsetOutOfBoundException() {
        super("Offset out of bound.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
