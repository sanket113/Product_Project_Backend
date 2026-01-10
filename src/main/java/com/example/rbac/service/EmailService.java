package com.example.rbac.service;


public interface EmailService {
    void sendOtpEmail(String to, String otp);
}
