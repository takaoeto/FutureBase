package com.example.futurebase.backend.service;

import com.example.futurebase.backend.entity.OtpToken;
import com.example.futurebase.backend.entity.User;
import com.example.futurebase.backend.exception.AppException;
import com.example.futurebase.backend.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    @Value("${app.otp.expiration-minutes}")
    private int expirationMinutes;

    @Value("${app.otp.max-attempts}")
    private int maxAttempts;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * OTPを生成・保存・メール送信する
     */
    @Transactional
    public void issueOtp(User user) {
        // 既存のアクティブOTPを削除
        otpRepository.deleteActiveByUserId(user.getId());

        String otp = generateOtp();
        String hash = BCrypt.hashpw(otp, BCrypt.gensalt());
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);

        OtpToken token = new OtpToken(user, hash, expiresAt);
        otpRepository.save(token);

        sendOtpMail(user.getEmail(), otp);
        log.info("OTP issued for user: {}", user.getId());
    }

    /**
     * 登録用OTP検証（アカウント有効化）
     * 
     * @throws AppException.OtpExpiredException  OTP期限切れ
     * @throws AppException.OtpMismatchException OTP不一致
     * @throws AppException.OtpLockedException   ロック
     */
    @Transactional
    public void verifyOtpForRegister(User user) {
        // ※ 呼び出し元で otp 文字列を渡す設計に変更
        // このメソッドは使わず verifyOtp を使う
    }

    /**
     * OTP検証
     */
    @Transactional
    public void verifyOtp(User user, String rawOtp) {
        OtpToken token = otpRepository.findLatestActiveByUserId(user.getId())
                .orElseThrow(AppException.OtpExpiredException::new);

        if (token.getAttemptCount() >= maxAttempts) {
            user.setLocked(true);
            throw new AppException.OtpLockedException();
        }

        if (token.isExpired()) {
            throw new AppException.OtpExpiredException();
        }

        if (!BCrypt.checkpw(rawOtp, token.getOtpHash())) {
            token.setAttemptCount(token.getAttemptCount() + 1);
            otpRepository.save(token);

            if (token.getAttemptCount() >= maxAttempts) {
                user.setLocked(true);
                throw new AppException.OtpLockedException();
            }
            throw new AppException.OtpMismatchException();
        }

        // 消費済みにする
        token.setConsumedAt(LocalDateTime.now());
        otpRepository.save(token);
    }

    /**
     * ログイン用OTP検証（OTP_INVALIDを返す）
     */
    @Transactional
    public void verifyOtpForLogin(User user, String rawOtp) {
        OtpToken token = otpRepository.findLatestActiveByUserId(user.getId())
                .orElseThrow(AppException.OtpInvalidException::new);

        if (token.getAttemptCount() >= maxAttempts) {
            user.setLocked(true);
            throw new AppException.AccountLockedException();
        }

        if (token.isExpired()) {
            throw new AppException.OtpInvalidException();
        }

        if (!BCrypt.checkpw(rawOtp, token.getOtpHash())) {
            token.setAttemptCount(token.getAttemptCount() + 1);
            otpRepository.save(token);

            if (token.getAttemptCount() >= maxAttempts) {
                user.setLocked(true);
                throw new AppException.AccountLockedException();
            }
            throw new AppException.OtpInvalidException();
        }

        token.setConsumedAt(LocalDateTime.now());
        otpRepository.save(token);
    }

    private String generateOtp() {
        int n = RANDOM.nextInt(1_000_000);
        return String.format("%06d", n);
    }

    private void sendOtpMail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("【FutureBase】認証コード");
            message.setText("認証コード: " + otp + "\n有効期限: " + expirationMinutes + "分");
            mailSender.send(message);
        } catch (Exception e) {
            // メール送信失敗はログのみ（開発環境ではコンソール出力で代替可）
            log.error("Failed to send OTP mail to {}: {}", to, e.getMessage());
        }
    }
}
