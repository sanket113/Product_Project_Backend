package com.example.rbac.service.impl;

import com.example.rbac.dto.LoginRequestDto;
import com.example.rbac.entity.PasswordResetOtp;
import com.example.rbac.entity.User;
import com.example.rbac.exception.AppException;
import com.example.rbac.exception.InvalidOtpException;
import com.example.rbac.exception.OtpAttemptsExceededException;
import com.example.rbac.exception.OtpExpiredException;
import com.example.rbac.repository.PasswordResetOtpRepository;
import com.example.rbac.repository.UserRepository;
import com.example.rbac.service.EmailService;
import com.example.rbac.service.PasswordResetService;
import com.example.rbac.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public void requestOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with this email"));

        String otp = OtpGenerator.generateOtp();

        PasswordResetOtp resetOtp = PasswordResetOtp.builder()
                .email(email)
                .otpHash(passwordEncoder.encode(otp))
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        otpRepository.save(resetOtp);
        emailService.sendOtpEmail(email, otp);
    }



    @Override
    public void confirmReset(String email, String otp, String newPassword) {

        PasswordResetOtp resetOtp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(InvalidOtpException::new);

        if (resetOtp.isUsed()) {
            throw new InvalidOtpException();
        }

        if (resetOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException();
        }

        if (resetOtp.getAttempts() >= 5) {
            throw new OtpAttemptsExceededException();
        }

        if (!passwordEncoder.matches(otp, resetOtp.getOtpHash())) {
            resetOtp.setAttempts(resetOtp.getAttempts() + 1);
            otpRepository.save(resetOtp);
            throw new InvalidOtpException();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidOtpException::new);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetOtp.setUsed(true);
        otpRepository.save(resetOtp);
    }
}
