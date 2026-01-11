package com.example.rbac.service;

import com.example.rbac.dto.LoginRequestDto;

public interface PasswordResetService {

    void requestOtp(String email);
    void confirmReset(String email, String otp, String newPassword);
}
