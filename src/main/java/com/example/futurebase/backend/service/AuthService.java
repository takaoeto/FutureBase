package com.example.futurebase.backend.service;

import com.example.futurebase.backend.dto.request.AuthRequest.*;
import com.example.futurebase.backend.dto.response.ApiResponse.TokenResponse;
import com.example.futurebase.backend.entity.User;
import com.example.futurebase.backend.exception.AppException;
import com.example.futurebase.backend.repository.UserRepository;
import com.example.futurebase.backend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtProvider jwtProvider;

    /**
     * 1. ユーザー登録: OTP送信
     */
    @Transactional
    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new AppException.EmailAlreadyExistsException();
        }

        String hash = passwordEncoder.encode(req.password());
        User user = new User(req.email(), hash);
        userRepository.save(user);

        otpService.issueOtp(user);
        log.info("User registered: {}", user.getId());
    }

    /**
     * 2. 登録OTP検証: アカウント有効化
     */
    @Transactional
    public void verifyRegisterOtp(OtpVerifyRequest req) {
        User user = findUserByEmailOrThrow404(req.email());
        otpService.verifyOtp(user, req.otp());
        user.setEnabled(true);
        userRepository.save(user);
        log.info("User verified: {}", user.getId());
    }

    /**
     * 3. ログイン: OTP送信
     */
    @Transactional
    public void login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(AppException.InvalidCredentialsException::new);
        //
        System.out.println("INPUT: " + req.password());
        System.out.println("DB HASH: " + user.getPasswordHash());
        System.out.println("MATCH RESULT: " +
                passwordEncoder.matches(req.password(), user.getPasswordHash()));

        if (user.isLocked()) {
            throw new AppException.AccountLockedException();
        }
        if (!user.isEnabled()) {
            throw new AppException.AccountNotVerifiedException();
        }
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new AppException.InvalidCredentialsException();
        }

        otpService.issueOtp(user);
        log.info("Login OTP sent: {}", user.getId());
    }

    /**
     * 4. ログインOTP検証: JWT発行
     */
    @Transactional
    public TokenResponse verifyLoginOtp(OtpVerifyRequest req) {
        User user = findUserByEmailOrThrow401(req.email());

        if (user.isLocked()) {
            throw new AppException.AccountLockedException();
        }

        otpService.verifyOtpForLogin(user, req.otp());

        String token = jwtProvider.generateToken(user.getId(), user.getEmail());
        log.info("JWT issued: {}", user.getId());

        return new TokenResponse(token, "Bearer", jwtProvider.getExpirationSeconds());
    }

    // ---- private helpers ----

    private User findUserByEmailOrThrow404(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(AppException.ResourceNotFoundException::new);
    }

    private User findUserByEmailOrThrow401(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(AppException.OtpInvalidException::new);
    }
}
