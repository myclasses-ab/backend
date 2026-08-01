package com.classes.Backend.Controller.auth;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.InstituteStaffRole;
import com.classes.Backend.Domain.enums.UserRole;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.auth.InstituteSignupService;
import com.classes.Backend.Service.auth.JwtService;
import com.classes.Backend.Service.institute.InstituteServiceImpl;
import com.classes.Backend.Service.mail.MailService;
import com.classes.Backend.Service.messagecentral.MessageCentralException;
import com.classes.Backend.Service.messagecentral.MessageCentralService;
import com.classes.Backend.Service.users.UserInstituteAssociationServiceImpl;
import com.classes.Backend.Service.users.UserService;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.auth.AuthResponse;
import com.classes.Backend.dto.auth.LoginRequest;
import com.classes.Backend.dto.auth.RegisterRequest;
import com.classes.Backend.dto.auth.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager AUTHENTICATION_MANAGER;
    private final UserService USER_SERVICE;
    private final JwtService JWT_SERVICE;
    private final UserDetailsService USER_DETAILS_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final InstituteServiceImpl INSTITUTE_SERVICE_IMPL;
    private final UserInstituteAssociationServiceImpl USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final MailService MAIL_SERVICE;
    private final MessageCentralService MESSAGE_CENTRAL_SERVICE;
    private final InstituteSignupService INSTITUTE_SIGNUP_SERVICE;

    // In-memory OTP storage with TTL cleanup
    private final ConcurrentHashMap<String, OtpEntry> OTP_STORE = new ConcurrentHashMap<>();
    private final ScheduledExecutorService OTP_CLEANUP_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    private static class OtpEntry {
        String verificationId;
        long expiresAt;

        OtpEntry(String verificationId, long expiresAt) {
            this.verificationId = verificationId;
            this.expiresAt = expiresAt;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AUTHENTICATION_MANAGER.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
        }

        User user = USER_SERVICE.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        user.setLastLoginAt(LocalDateTime.now());
        USER_SERVICE.save(user);

        UserDetails userDetails = USER_DETAILS_SERVICE.loadUserByUsername(request.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getIdentifier());
        claims.put("role", user.getRole().name());

        String token = JWT_SERVICE.generateToken(claims, userDetails);

        ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                .actorType(mapRoleToActorType(user.getRole()))
                .actorIdentifier(user.getIdentifier())
                .actorName(user.getFullName())
                .actionType(ActivityActionType.LOGIN)
                .entityType(ActivityEntityType.USER)
                .entityIdentifier(user.getIdentifier())
                .description("Logged in with email")
                .source(resolveSourceFromRole(user.getRole()))
                .build());

        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (USER_SERVICE.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(PASSWORD_ENCODER.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.INSTITUTE_ADMIN);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        User savedUser = USER_SERVICE.save(user);

        UserDetails userDetails = USER_DETAILS_SERVICE.loadUserByUsername(savedUser.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getIdentifier());
        claims.put("role", savedUser.getRole().name());

        String token = JWT_SERVICE.generateToken(claims, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, savedUser));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        String normalizedPhone = normalizeSignupPhone(request.getPhone());

        InstituteSignupService.PendingSignup pending;
        try {
            pending = INSTITUTE_SIGNUP_SERVICE.getPending(request.getEmail());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Please complete email and phone verification before signing up."));
        }

        if (!pending.emailVerified || !pending.phoneVerified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Please verify both email and phone before completing signup."));
        }

        if (!pending.email.equalsIgnoreCase(request.getEmail())
                || !pending.phone.equals(normalizedPhone)
                || !pending.instituteName.equals(request.getInstituteName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Signup details do not match the verified session."));
        }

        if (USER_SERVICE.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
        }
        if (USER_SERVICE.existsByPhone(normalizedPhone)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Phone number already registered"));
        }

        // 1. Create User
        User user = new User();
        user.setFullName(request.getInstituteName());
        user.setEmail(request.getEmail());
        user.setPhone(normalizedPhone);
        user.setPasswordHash(PASSWORD_ENCODER.encode(request.getPassword()));
        user.setRole(UserRole.INSTITUTE_ADMIN);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);

        User savedUser = USER_SERVICE.save(user);

        // 2. Generate slug from institute name
        String baseSlug = generateSlug(request.getInstituteName());
        String slug = baseSlug;
        int attempts = 0;
        while (INSTITUTE_SERVICE_IMPL.findBySlug(slug).isPresent() && attempts < 100) {
            slug = baseSlug + "-" + String.format("%02d", new Random().nextInt(100));
            attempts++;
        }

        // 3. Create Institute with UUID identifier
        Institute institute = new Institute();
        institute.setIdentifier(UUID.randomUUID().toString());  // UUID for identifier
        institute.setName(request.getInstituteName());
        institute.setSlug(slug);  // Slug for SEO-friendly URLs
        institute.setEmail(request.getEmail());
        institute.setPhonePrimary(normalizedPhone);
        institute.setCreatedBy(savedUser.getIdentifier());

        Institute savedInstitute = INSTITUTE_SERVICE_IMPL.save(institute);

        // 4. Create User-Institute Association
        UserInstituteAssociation association = new UserInstituteAssociation();
        association.setUserIdentifier(savedUser.getIdentifier());
        association.setInstituteIdentifier(savedInstitute.getIdentifier());
        association.setRole(InstituteStaffRole.OWNER);

        USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.save(association);

        // 5. Remove pending signup
        INSTITUTE_SIGNUP_SERVICE.removePending(request.getEmail());

        // 6. Send welcome email (async — will not block signup response)
        MAIL_SERVICE.sendInstituteWelcomeEmail(savedUser.getEmail(), savedInstitute.getName(), savedUser.getEmail());

        // 7. Generate token
        UserDetails userDetails = USER_DETAILS_SERVICE.loadUserByUsername(savedUser.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getIdentifier());
        claims.put("role", savedUser.getRole().name());

        String token = JWT_SERVICE.generateToken(claims, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, savedUser));
    }

    @PostMapping("/signup/initiate")
    public ResponseEntity<?> initiateSignup(@RequestBody com.classes.Backend.dto.auth.InstituteSignupInitiateRequest request) {
        try {
            String normalizedPhone = normalizeSignupPhone(request.getPhone());
            if (USER_SERVICE.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
            }
            if (USER_SERVICE.existsByPhone(normalizedPhone)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Phone number already registered"));
            }

            InstituteSignupService.PendingSignup pending = INSTITUTE_SIGNUP_SERVICE.initiate(request);

            return ResponseEntity.ok(Map.of(
                    "message", "Verification code sent to your email. Please verify your email first.",
                    "email", maskEmail(pending.email),
                    "phone", maskPhone(pending.phone),
                    "expiresInMinutes", 10
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (MessageCentralException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signup/verify-email")
    public ResponseEntity<?> verifySignupEmail(@RequestBody com.classes.Backend.dto.auth.InstituteSignupVerifyRequest request) {
        try {
            INSTITUTE_SIGNUP_SERVICE.verifyEmail(request.getEmail(), request.getCode());
            return ResponseEntity.ok(Map.of(
                    "message", "Email verified successfully. You can now request a phone OTP.",
                    "emailVerified", true
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signup/send-phone-otp")
    public ResponseEntity<?> sendSignupPhoneOtp(@RequestBody Map<String, String> request) {
        try {
            InstituteSignupService.PendingSignup pending = INSTITUTE_SIGNUP_SERVICE.sendPhoneOtp(request.get("email"));
            return ResponseEntity.ok(Map.of(
                    "message", "Phone OTP sent.",
                    "phone", maskPhone(pending.phone),
                    "expiresInMinutes", 10
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (MessageCentralException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signup/verify-phone")
    public ResponseEntity<?> verifySignupPhone(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        try {
            INSTITUTE_SIGNUP_SERVICE.verifyPhone(email, otp);
            InstituteSignupService.PendingSignup pending = INSTITUTE_SIGNUP_SERVICE.getPending(email);
            return ResponseEntity.ok(Map.of(
                    "message", "Phone verified successfully.",
                    "emailVerified", pending.emailVerified,
                    "phoneVerified", true
            ));
        } catch (IllegalArgumentException | IllegalStateException | MessageCentralException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signup/resend-email")
    public ResponseEntity<?> resendSignupEmail(@RequestBody Map<String, String> request) {
        try {
            INSTITUTE_SIGNUP_SERVICE.resendEmailCode(request.get("email"));
            return ResponseEntity.ok(Map.of("message", "Email verification code resent."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signup/resend-phone")
    public ResponseEntity<?> resendSignupPhone(@RequestBody Map<String, String> request) {
        try {
            INSTITUTE_SIGNUP_SERVICE.resendPhoneOtp(request.get("email"));
            return ResponseEntity.ok(Map.of("message", "Phone OTP resent."));
        } catch (IllegalArgumentException | IllegalStateException | MessageCentralException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/super-admin-login")
    public ResponseEntity<?> superAdminLogin(@RequestBody LoginRequest request) {
        if (!"aditya@gmail.com".equals(request.getEmail()) || !"aditya".equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid super admin credentials"));
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("aditya@gmail.com")
                .password("")
                .authorities("ROLE_SUPER_ADMIN")
                .build();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "super-admin");
        claims.put("role", UserRole.SUPER_ADMIN.name());

        String token = JWT_SERVICE.generateToken(claims, userDetails);

        User user = new User();
        user.setIdentifier("super-admin");
        user.setFullName("Super Admin");
        user.setEmail("aditya@gmail.com");
        user.setRole(UserRole.SUPER_ADMIN);
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setPreferredLanguage("English");
        user.setLastLoginAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing or invalid token"));
        }

        String token = authHeader.substring(7);
        String username = JWT_SERVICE.extractUsername(token);

        // Hardcoded super admin — not stored in DB
        if ("aditya@gmail.com".equals(username)) {
            User user = new User();
            user.setIdentifier("super-admin");
            user.setFullName("Super Admin");
            user.setEmail("aditya@gmail.com");
            user.setRole(UserRole.SUPER_ADMIN);
            user.setIsActive(true);
            user.setEmailVerified(true);
            user.setPhoneVerified(true);
            user.setPreferredLanguage("English");
            return ResponseEntity.ok(user);
        }

        User user = USER_SERVICE.findByEmail(username)
                .orElseGet(() -> USER_SERVICE.findByPhone(username)
                        .orElseThrow(() -> new BadCredentialsException("User not found")));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/me/book-demo")
    public ResponseEntity<?> bookDemo(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody Map<String, String> request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing or invalid token"));
        }

        String courseIdentifier = request.get("courseIdentifier");
        if (courseIdentifier == null || courseIdentifier.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "courseIdentifier is required"));
        }

        String token = authHeader.substring(7);
        String username = JWT_SERVICE.extractUsername(token);

        if ("aditya@gmail.com".equals(username)) {
            User user = new User();
            user.setIdentifier("super-admin");
            user.setFullName("Super Admin");
            user.setEmail("aditya@gmail.com");
            user.setRole(UserRole.SUPER_ADMIN);
            user.setIsActive(true);
            user.setEmailVerified(true);
            user.setPhoneVerified(true);
            user.setPreferredLanguage("English");
            return ResponseEntity.ok(user);
        }

        User user = USER_SERVICE.findByEmail(username)
                .orElseGet(() -> USER_SERVICE.findByPhone(username)
                        .orElseThrow(() -> new BadCredentialsException("User not found")));

        List<String> booked = user.getBookedDemoCourseIdentifiers();
        if (booked == null) {
            booked = new java.util.ArrayList<>();
        }
        if (!booked.contains(courseIdentifier)) {
            booked.add(courseIdentifier);
            user.setBookedDemoCourseIdentifiers(booked);
            USER_SERVICE.save(user);
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing or invalid token"));
        }

        String token = authHeader.substring(7);
        String username = JWT_SERVICE.extractUsername(token);

        UserDetails userDetails = USER_DETAILS_SERVICE.loadUserByUsername(username);
        User user = USER_SERVICE.findByEmail(username)
                .orElseGet(() -> USER_SERVICE.findByPhone(username)
                        .orElseThrow(() -> new BadCredentialsException("User not found")));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getIdentifier());
        claims.put("role", user.getRole().name());

        String newToken = JWT_SERVICE.generateToken(claims, userDetails);
        return ResponseEntity.ok(new AuthResponse(newToken, user));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody com.classes.Backend.dto.auth.OtpRequest request) {
        String phone = request.getPhone();
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number is required"));
        }

        // Normalize phone (remove spaces, ensure +91 prefix)
        phone = phone.trim().replaceAll("\\s+", "");
        if (!phone.startsWith("+")) {
            phone = "+91" + phone;
        }

        // Rate limit check: 1 request per 60 seconds per phone
        OtpEntry existing = OTP_STORE.get(phone);
        if (existing != null && System.currentTimeMillis() < existing.expiresAt - 4 * 60 * 1000) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Please wait before requesting a new OTP"));
        }

        // Send real OTP via Message Central
        String mobileNumber = phone.startsWith("+") ? phone.substring(1) : phone;
        if (mobileNumber.startsWith(countryCode())) {
            mobileNumber = mobileNumber.substring(countryCode().length());
        }

        String verificationId;
        try {
            verificationId = MESSAGE_CENTRAL_SERVICE.sendOtp(mobileNumber);
        } catch (MessageCentralException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }

        long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes
        OTP_STORE.put(phone, new OtpEntry(verificationId, expiry));

        // Schedule cleanup
        final String phoneKey = phone;
        OTP_CLEANUP_EXECUTOR.schedule(() -> OTP_STORE.remove(phoneKey), 5, TimeUnit.MINUTES);

        System.out.println("[OTP] Phone: " + phone + " | verificationId: " + verificationId);

        boolean isRegistered = USER_SERVICE.findByPhone(phone).isPresent();

        return ResponseEntity.ok(Map.of(
                "message", "OTP sent successfully",
                "phone", phone,
                "expiresIn", 300,
                "isRegistered", isRegistered
        ));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody com.classes.Backend.dto.auth.OtpVerifyRequest request) {
        String phone = request.getPhone();
        String otp = request.getOtp();

        if (phone == null || phone.isBlank() || otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone and OTP are required"));
        }

        phone = phone.trim().replaceAll("\\s+", "");
        if (!phone.startsWith("+")) {
            phone = "+91" + phone;
        }

        OtpEntry entry = OTP_STORE.get(phone);
        if (entry == null || System.currentTimeMillis() > entry.expiresAt) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "OTP expired or not found"));
        }

        String mobileNumber = phone.startsWith("+") ? phone.substring(1) : phone;
        if (mobileNumber.startsWith(countryCode())) {
            mobileNumber = mobileNumber.substring(countryCode().length());
        }

        boolean verified;
        try {
            verified = MESSAGE_CENTRAL_SERVICE.verifyOtp(mobileNumber, otp, entry.verificationId);
        } catch (MessageCentralException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }

        if (!verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid OTP"));
        }

        // OTP verified — find or create user
        boolean isNewUser = false;
        User user = USER_SERVICE.findByPhone(phone).orElse(null);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setFullName(request.getFullName() != null && !request.getFullName().isBlank() ? request.getFullName() : "Student");
            user.setRole(UserRole.STUDENT);
            user.setPhoneVerified(true);
            user.setEmailVerified(false);
            user.setIsActive(true);
            user = USER_SERVICE.save(user);
            isNewUser = true;
        } else {
            user.setPhoneVerified(true);
            user.setLastLoginAt(LocalDateTime.now());
            // Update name if provided and currently default
            if (request.getFullName() != null && !request.getFullName().isBlank() 
                && (user.getFullName() == null || user.getFullName().equals("Student"))) {
                user.setFullName(request.getFullName());
            }
            user = USER_SERVICE.save(user);
        }

        // Remove used OTP
        OTP_STORE.remove(phone);

        ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                .actorType(ActivityActorType.STUDENT)
                .actorIdentifier(user.getIdentifier())
                .actorName(user.getFullName())
                .actionType(isNewUser ? ActivityActionType.STUDENT_REGISTERED : ActivityActionType.LOGIN_OTP)
                .entityType(ActivityEntityType.USER)
                .entityIdentifier(user.getIdentifier())
                .description(isNewUser ? "Student registered via OTP" : "Logged in via OTP")
                .source("FRONTEND")
                .build());

        // Generate JWT with phone as subject
        UserDetails userDetails = USER_DETAILS_SERVICE.loadUserByUsername(phone);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getIdentifier());
        claims.put("role", user.getRole().name());
        claims.put("authType", "phone");

        String token = JWT_SERVICE.generateToken(claims, userDetails);
        return ResponseEntity.ok(new com.classes.Backend.dto.auth.PhoneAuthResponse(token, user, isNewUser));
    }

    private String countryCode() {
        return "91";
    }

    private String generateSlug(String instituteName) {
        if (instituteName == null || instituteName.trim().isEmpty()) {
            return "institute";
        }
        String slug = instituteName.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-*|-*$", "");
        return slug.isEmpty() ? "institute" : slug;
    }

    private ActivityActorType mapRoleToActorType(UserRole role) {
        if (role == null) {
            return ActivityActorType.STUDENT;
        }
        return switch (role) {
            case INSTITUTE_ADMIN -> ActivityActorType.INSTITUTE_ADMIN;
            case INSTITUTE_STAFF -> ActivityActorType.INSTITUTE_STAFF;
            case SUPER_ADMIN, CONTENT_MANAGER -> ActivityActorType.SUPER_ADMIN;
            default -> ActivityActorType.STUDENT;
        };
    }

    private String resolveSourceFromRole(UserRole role) {
        if (role == null) {
            return "FRONTEND";
        }
        return switch (role) {
            case SUPER_ADMIN, CONTENT_MANAGER -> "SUPER_ADMIN";
            case INSTITUTE_ADMIN, INSTITUTE_STAFF -> "CONSOLE";
            default -> "FRONTEND";
        };
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

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 8) return phone;
        return phone.substring(0, 4) + " **** " + phone.substring(phone.length() - 2);
    }

    private String normalizeSignupPhone(String phone) {
        if (phone == null) return null;
        phone = phone.trim().replaceAll("\\s+", "").replaceAll("[^0-9+]", "");
        if (!phone.startsWith("+")) {
            phone = "+91" + phone;
        }
        return phone;
    }
}
