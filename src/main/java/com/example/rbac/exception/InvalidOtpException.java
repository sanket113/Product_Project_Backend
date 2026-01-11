package com.example.rbac.exception;

public class InvalidOtpException extends RuntimeException {

    public InvalidOtpException() {
        super("Invalid or expired OTP");
    }
}
