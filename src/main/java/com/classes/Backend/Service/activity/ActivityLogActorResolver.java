package com.classes.Backend.Service.activity;

import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.enums.UserRole;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.auth.JwtService;
import com.classes.Backend.Service.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivityLogActorResolver {

    private final JwtService jwtService;
    private final UserService userService;

    public ResolvedActor resolve(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthenticated();
        }
        return resolveFromToken(authHeader.substring(7));
    }

    public ResolvedActor resolveFromToken(String token) {
        if (token == null || token.isBlank()) {
            return unauthenticated();
        }
        try {
            String username = jwtService.extractUsername(token);

            if ("aditya@gmail.com".equals(username)) {
                return ResolvedActor.builder()
                        .type(ActivityActorType.SUPER_ADMIN)
                        .identifier("super-admin")
                        .name("Super Admin")
                        .authenticated(true)
                        .build();
            }

            User user = userService.findByEmail(username)
                    .orElseGet(() -> userService.findByPhone(username).orElse(null));

            if (user == null) {
                return unauthenticated();
            }

            return ResolvedActor.builder()
                    .type(mapRole(user.getRole()))
                    .identifier(user.getIdentifier())
                    .name(user.getFullName())
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            return unauthenticated();
        }
    }

    private ResolvedActor unauthenticated() {
        return ResolvedActor.builder()
                .authenticated(false)
                .build();
    }

    private ActivityActorType mapRole(UserRole role) {
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
}
