package com.classes.Backend.Service.auth;

import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService USER_SERVICE;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Hardcoded super admin — not stored in DB
        if ("aditya@gmail.com".equals(username)) {
            return new org.springframework.security.core.userdetails.User(
                    "aditya@gmail.com",
                    "",
                    true,
                    true,
                    true,
                    true,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))
            );
        }

        User user = USER_SERVICE.findByEmail(username)
                .orElseGet(() -> USER_SERVICE.findByPhone(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));

        String password = user.getPasswordHash() != null ? user.getPasswordHash() : "PHONE_AUTH_DUMMY_PASSWORD";
        String principal = user.getEmail() != null ? user.getEmail() : user.getPhone();

        return new org.springframework.security.core.userdetails.User(
                principal,
                password,
                user.getIsActive() != null && user.getIsActive(),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
