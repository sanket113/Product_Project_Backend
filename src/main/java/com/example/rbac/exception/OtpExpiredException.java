package com.example.rbac.exception;

public class OtpExpiredException extends RuntimeException {

    public OtpExpiredException() {
        super("Invalid or expired OTP");
    }
}
