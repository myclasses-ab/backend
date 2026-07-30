package com.classes.Backend.Service.auth;

import com.classes.Backend.Service.mail.MailService;
import com.classes.Backend.Service.messagecentral.MessageCentralException;
import com.classes.Backend.Service.messagecentral.MessageCentralService;
import com.classes.Backend.dto.auth.InstituteSignupInitiateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles email + phone verification for institute signups.
 * Signups are kept in a pending state until both channels are verified.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InstituteSignupService {

    private final MailService MAIL_SERVICE;
    private final MessageCentralService MESSAGE_CENTRAL_SERVICE;

    private static final int CODE_LENGTH = 6;
    private static final long EXPIRY_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 5;

    private final Map<String, PendingSignup> pendingSignups = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new SecureRandom();

    public static class PendingSignup {
        public String email;
        public String phone;
        public String password;
        public String instituteName;
        public String emailCode;
        public String phoneVerificationId;
        public boolean emailVerified;
        public boolean phoneVerified;
        public int emailAttempts;
        public int phoneAttempts;
        public Instant expiresAt;
    }

    /**
     * Start a new institute signup: validate input and send email verification code.
     * Phone OTP is sent only after email is verified.
     */
    public PendingSignup initiate(InstituteSignupInitiateRequest request) {
        String email = normalizeEmail(request.getEmail());
        String phone = normalizePhone(request.getPhone());
        String password = request.getPassword();
        String instituteName = request.getInstituteName();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (instituteName == null || instituteName.trim().length() < 2) {
            throw new IllegalArgumentException("Institute name is required");
        }

        String emailCode = generateCode();
        MAIL_SERVICE.sendEmailVerificationCode(email, instituteName, emailCode);

        PendingSignup pending = new PendingSignup();
        pending.email = email;
        pending.phone = phone;
        pending.password = password;
        pending.instituteName = instituteName;
        pending.emailCode = emailCode;
        pending.phoneVerificationId = null;
        pending.emailVerified = false;
        pending.phoneVerified = false;
        pending.emailAttempts = 0;
        pending.phoneAttempts = 0;
        pending.expiresAt = Instant.now().plus(EXPIRY_MINUTES, ChronoUnit.MINUTES);

        pendingSignups.put(email, pending);
        scheduleCleanup(email);

        log.info("Institute signup initiated for email={} phone={}", maskEmail(email), maskPhone(phone));
        return pending;
    }

    /**
     * Send phone OTP for a pending signup after email is verified.
     */
    public PendingSignup sendPhoneOtp(String email) {
        email = normalizeEmail(email);
        PendingSignup pending = getPending(email);

        if (!pending.emailVerified) {
            throw new IllegalStateException("Please verify your email before requesting phone OTP.");
        }

        if (pending.phoneVerificationId != null && !pending.phoneVerificationId.isBlank()) {
            // Phone OTP already sent; reuse existing verificationId without resending.
            return pending;
        }

        String mobileNumber = pending.phone.startsWith("+") ? pending.phone.substring(1) : pending.phone;
        if (mobileNumber.startsWith("91")) {
            mobileNumber = mobileNumber.substring(2);
        }

        String phoneVerificationId;
        try {
            phoneVerificationId = MESSAGE_CENTRAL_SERVICE.sendOtp(mobileNumber);
        } catch (MessageCentralException e) {
            log.error("Failed to send phone OTP for institute signup: {}", email, e);
            throw new MessageCentralException("Failed to send phone OTP. " + e.getMessage(), e);
        }

        pending.phoneVerificationId = phoneVerificationId;
        pending.phoneAttempts = 0;
        pending.expiresAt = Instant.now().plus(EXPIRY_MINUTES, ChronoUnit.MINUTES);

        log.info("Phone OTP sent for institute signup email={} phone={}", maskEmail(email), maskPhone(pending.phone));
        return pending;
    }

    /**
     * Verify the email code for a pending signup.
     */
    public boolean verifyEmail(String email, String code) {
        email = normalizeEmail(email);
        PendingSignup pending = getPending(email);

        if (pending.emailVerified) {
            return true;
        }

        if (pending.emailAttempts >= MAX_ATTEMPTS) {
            throw new IllegalStateException("Too many attempts. Please request a new code.");
        }

        pending.emailAttempts++;
        if (pending.emailCode == null || !pending.emailCode.equals(code)) {
            throw new IllegalArgumentException("Invalid email verification code");
        }

        pending.emailVerified = true;
        return true;
    }

    /**
     * Verify the phone OTP for a pending signup.
     */
    public boolean verifyPhone(String email, String otp) {
        email = normalizeEmail(email);
        PendingSignup pending = getPending(email);

        if (pending.phoneVerified) {
            return true;
        }

        if (pending.phoneAttempts >= MAX_ATTEMPTS) {
            throw new IllegalStateException("Too many attempts. Please request a new OTP.");
        }

        pending.phoneAttempts++;

        if (pending.phoneVerificationId == null || pending.phoneVerificationId.isBlank()) {
            throw new IllegalStateException("Phone OTP has not been sent. Please request phone OTP first.");
        }

        String mobileNumber = pending.phone.startsWith("+") ? pending.phone.substring(1) : pending.phone;
        if (mobileNumber.startsWith("91")) {
            mobileNumber = mobileNumber.substring(2);
        }

        boolean verified;
        try {
            verified = MESSAGE_CENTRAL_SERVICE.verifyOtp(mobileNumber, otp, pending.phoneVerificationId);
        } catch (MessageCentralException e) {
            log.warn("Phone OTP verification failed for email={}: {}", maskEmail(email), e.getMessage());
            throw new MessageCentralException("Invalid phone OTP. " + e.getMessage(), e);
        }

        if (verified) {
            pending.phoneVerified = true;
        }
        return verified;
    }

    /**
     * Resend email verification code.
     */
    public void resendEmailCode(String email) {
        email = normalizeEmail(email);
        PendingSignup pending = getPending(email);
        String newCode = generateCode();
        pending.emailCode = newCode;
        pending.emailAttempts = 0;
        pending.expiresAt = Instant.now().plus(EXPIRY_MINUTES, ChronoUnit.MINUTES);
        MAIL_SERVICE.sendEmailVerificationCode(pending.email, pending.instituteName, newCode);
    }

    /**
     * Resend phone OTP.
     */
    public void resendPhoneOtp(String email) {
        email = normalizeEmail(email);
        PendingSignup pending = getPending(email);

        String mobileNumber = pending.phone.startsWith("+") ? pending.phone.substring(1) : pending.phone;
        if (mobileNumber.startsWith("91")) {
            mobileNumber = mobileNumber.substring(2);
        }

        String verificationId;
        try {
            verificationId = MESSAGE_CENTRAL_SERVICE.sendOtp(mobileNumber);
        } catch (MessageCentralException e) {
            throw new MessageCentralException("Failed to resend phone OTP. " + e.getMessage(), e);
        }

        pending.phoneVerificationId = verificationId;
        pending.phoneAttempts = 0;
        pending.expiresAt = Instant.now().plus(EXPIRY_MINUTES, ChronoUnit.MINUTES);
    }

    /**
     * Retrieve and validate a pending signup.
     */
    public PendingSignup getPending(String email) {
        email = normalizeEmail(email);
        PendingSignup pending = pendingSignups.get(email);
        if (pending == null) {
            throw new IllegalStateException("No pending signup found. Please start again.");
        }
        if (Instant.now().isAfter(pending.expiresAt)) {
            pendingSignups.remove(email);
            throw new IllegalStateException("Verification session expired. Please start again.");
        }
        return pending;
    }

    /**
     * Remove a pending signup (e.g. after completion or explicit cancel).
     */
    public void removePending(String email) {
        pendingSignups.remove(normalizeEmail(email));
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

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        phone = phone.trim().replaceAll("\\s+", "").replaceAll("[^0-9+]", "");
        if (!phone.startsWith("+")) {
            phone = "+91" + phone;
        }
        return phone;
    }

    private void scheduleCleanup(String email) {
        cleanupExecutor.schedule(() -> pendingSignups.remove(email), EXPIRY_MINUTES, TimeUnit.MINUTES);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];
        String maskedLocal = local.length() <= 2 ? local : local.charAt(0) + "***" + local.charAt(local.length() - 1);
        return maskedLocal + "@" + domain;
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return phone;
        return phone.substring(0, 2) + " **** " + phone.substring(phone.length() - 2);
    }
}
