package com.example.rbac.controller;



import com.example.rbac.dto.PasswordResetConfirmDto;
import com.example.rbac.dto.PasswordResetRequestDto;
import com.example.rbac.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;



    @PostMapping("/request")
    public ResponseEntity<?> requestOtp(
            @Valid @RequestBody PasswordResetRequestDto dto) {

        passwordResetService.requestOtp(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmReset(
            @Valid @RequestBody PasswordResetConfirmDto dto) {

        passwordResetService.confirmReset(
                dto.getEmail(),
                dto.getOtp(),
                dto.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}
