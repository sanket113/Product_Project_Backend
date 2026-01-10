package com.example.rbac.exception;

public class OtpAttemptsExceededException extends RuntimeException {

    public OtpAttemptsExceededException() {
        super("Invalid or expired OTP");
    }
}
