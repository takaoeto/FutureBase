package com.example.futurebase.backend.controller;

import com.example.futurebase.backend.dto.request.AuthRequest.*;
import com.example.futurebase.backend.dto.response.ApiResponse.*;
import com.example.futurebase.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * ユーザー登録（OTP送信）
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(201).body(new MessageResponse("OTP sent"));
    }

    /**
     * POST /api/auth/otp/verify
     * 登録OTP検証（アカウント有効化）
     */
    @PostMapping("/otp/verify")
    public ResponseEntity<MessageResponse> verifyRegisterOtp(
            @Valid @RequestBody OtpVerifyRequest req) {
        authService.verifyRegisterOtp(req);
        return ResponseEntity.ok(new MessageResponse("Account verified"));
    }

    /**
     * POST /api/auth/login
     * ログイン（OTP送信）
     */
    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest req) {
        authService.login(req);
        return ResponseEntity.ok(new MessageResponse("OTP sent"));
    }

    /**
     * POST /api/auth/login/otp/verify
     * ログインOTP検証（JWT発行）
     */
    @PostMapping("/login/otp/verify")
    public ResponseEntity<TokenResponse> verifyLoginOtp(
            @Valid @RequestBody OtpVerifyRequest req) {
        TokenResponse token = authService.verifyLoginOtp(req);
        return ResponseEntity.ok(token);
    }

    /**
     * POST /api/auth/logout
     * ログアウト（サーバは何もしない）
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        return ResponseEntity.ok(new MessageResponse("Logged out"));
    }
}
