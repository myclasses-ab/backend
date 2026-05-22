package com.classes.Backend.config;

import com.classes.Backend.Domain.enums.InstituteStaffRole;
import com.classes.Backend.Domain.enums.UserRole;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Service.institute.InstituteService;
import com.classes.Backend.Service.users.UserInstituteAssociationService;
import com.classes.Backend.Service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserService USER_SERVICE;
    private final UserInstituteAssociationService USER_INSTITUTE_ASSOCIATION_SERVICE;
    private final InstituteService INSTITUTE_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Bean
    public CommandLineRunner seedAdminUser() {
        return args -> {
            String adminEmail = "admin@myclasses.com";
            if (!USER_SERVICE.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setFullName("Institute Admin");
                admin.setEmail(adminEmail);
                admin.setPhone("+919999999999");
                admin.setPasswordHash(PASSWORD_ENCODER.encode("admin123"));
                admin.setRole(UserRole.INSTITUTE_ADMIN);
                admin.setIsActive(true);
                User savedAdmin = USER_SERVICE.save(admin);

                // Link to first institute if any exist
                var institutes = INSTITUTE_SERVICE.findAll();
                if (!institutes.isEmpty()) {
                    String instituteId = institutes.get(0).getIdentifier();
                    UserInstituteAssociation association = new UserInstituteAssociation();
                    association.setUserIdentifier(savedAdmin.getIdentifier());
                    association.setInstituteIdentifier(instituteId);
                    association.setRole(InstituteStaffRole.OWNER);
                    association.setIsActive(true);
                    USER_INSTITUTE_ASSOCIATION_SERVICE.save(association);
                }
            }
        };
    }
}
