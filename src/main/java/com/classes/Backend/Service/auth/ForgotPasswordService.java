package com.classes.Backend.Service.auth;

import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.mail.MailService;
import com.classes.Backend.Service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles email OTP based password reset for existing users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final MailService MAIL_SERVICE;
    private final UserService USER_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;

    private static final int CODE_LENGTH = 6;
    private static final long EXPIRY_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 5;
    private static final long RESEND_COOLDOWN_SECONDS = 60;

    private final ConcurrentHashMap<String, ResetOtpEntry> otpStore = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new SecureRandom();

    private static class ResetOtpEntry {
        String code;
        String email;
        long expiresAt;
        long lastSentAt;
        int attempts;

        ResetOtpEntry(String code, String email, long expiresAt, long lastSentAt) {
            this.code = code;
            this.email = email;
            this.expiresAt = expiresAt;
            this.lastSentAt = lastSentAt;
            this.attempts = 0;
        }
    }

    /**
     * Send a password reset OTP to the given email if a user exists.
     */
    public void sendOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null || normalizedEmail.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        User user = USER_SERVICE.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("No account found with this email"));

        ResetOtpEntry existing = otpStore.get(normalizedEmail);
        long now = System.currentTimeMillis();
        if (existing != null && now - existing.lastSentAt < RESEND_COOLDOWN_SECONDS * 1000) {
            long waitSeconds = (RESEND_COOLDOWN_SECONDS * 1000 - (now - existing.lastSentAt)) / 1000;
            throw new IllegalStateException("Please wait " + waitSeconds + " seconds before requesting a new code");
        }

        String code = generateCode();
        long expiry = now + EXPIRY_MINUTES * 60 * 1000;
        otpStore.put(normalizedEmail, new ResetOtpEntry(code, normalizedEmail, expiry, now));
        scheduleCleanup(normalizedEmail);

        String name = user.getFullName() != null ? user.getFullName() : normalizedEmail;
        MAIL_SERVICE.sendPasswordResetOtp(normalizedEmail, name, code);

        log.info("Password reset OTP sent to {}", maskEmail(normalizedEmail));
    }

    /**
     * Resend a password reset OTP to the given email.
     */
    public void resendOtp(String email) {
        sendOtp(email);
    }

    /**
     * Verify the password reset OTP without resetting the password.
     */
    public boolean verifyOtp(String email, String code) {
        String normalizedEmail = normalizeEmail(email);
        ResetOtpEntry entry = getValidEntry(normalizedEmail);

        if (entry.attempts >= MAX_ATTEMPTS) {
            otpStore.remove(normalizedEmail);
            throw new IllegalStateException("Too many attempts. Please request a new code.");
        }

        entry.attempts++;
        if (!entry.code.equals(code)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        return true;
    }

    /**
     * Verify the OTP and reset the user's password.
     */
    public void resetPassword(String email, String code, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        String normalizedEmail = normalizeEmail(email);
        verifyOtp(normalizedEmail, code);

        User user = USER_SERVICE.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setPasswordHash(PASSWORD_ENCODER.encode(newPassword));
        USER_SERVICE.save(user);

        otpStore.remove(normalizedEmail);
        log.info("Password reset successfully for {}", maskEmail(normalizedEmail));
    }

    private ResetOtpEntry getValidEntry(String normalizedEmail) {
        ResetOtpEntry entry = otpStore.get(normalizedEmail);
        if (entry == null) {
            throw new IllegalStateException("No reset request found. Please start again.");
        }
        if (System.currentTimeMillis() > entry.expiresAt) {
            otpStore.remove(normalizedEmail);
            throw new IllegalStateException("Verification code expired. Please request a new code.");
        }
        return entry;
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }

    private void scheduleCleanup(String email) {
        cleanupExecutor.schedule(() -> otpStore.remove(email), EXPIRY_MINUTES, TimeUnit.MINUTES);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];
        String maskedLocal = local.length() <= 2 ? local
                : local.charAt(0) + "***" + local.charAt(local.length() - 1);
        return maskedLocal + "@" + domain;
    }
}
