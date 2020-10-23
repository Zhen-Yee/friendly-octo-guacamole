package com.example.demo.exception;

public class SettingNotFoundException extends RuntimeException {

    public SettingNotFoundException() {
        super("Setting ID not found.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
