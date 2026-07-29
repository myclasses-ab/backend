package com.classes.Backend.Controller.users;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.UserRole;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.users.UserServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.users.UserActivityTrackRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl USER_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE USER ===================== //
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.save(user), HttpStatus.CREATED);
    }

    // ================ CREATE ALL USERS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllUsers(@RequestBody List<User> users) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.saveAll(users), HttpStatus.CREATED);
    }

    // ================ GET USER BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getUserById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL USERS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = this.USER_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    // ================ DELETE USER BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteUserById(@PathVariable String identifier) {
        this.USER_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE USER BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateUserById(@PathVariable String identifier, @RequestBody User user) {
        if (!this.USER_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        user.setIdentifier(identifier);
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.save(user), HttpStatus.OK);
    }

    // ================ FIND BY EMAIL ===================== //
    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.findByEmail(email), HttpStatus.OK);
    }

    // ================ FIND BY PHONE ===================== //
    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> findByPhone(@PathVariable String phone) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.findByPhone(phone), HttpStatus.OK);
    }

    // ================ FIND BY ROLE ===================== //
    @GetMapping("/role/{role}")
    public ResponseEntity<?> findByRole(@PathVariable UserRole role) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.findByRole(role), HttpStatus.OK);
    }

    // ================ FIND BY CITY IDENTIFIER ===================== //
    @GetMapping("/city/{cityIdentifier}")
    public ResponseEntity<?> findByCityIdentifier(@PathVariable String cityIdentifier) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.findByCityIdentifier(cityIdentifier), HttpStatus.OK);
    }

    // ================ FIND ACTIVE USERS ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }

    // ================ CHECK EMAIL EXISTS ===================== //
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<?> existsByEmail(@PathVariable String email) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.existsByEmail(email), HttpStatus.OK);
    }

    // ================ CHECK PHONE EXISTS ===================== //
    @GetMapping("/exists/phone/{phone}")
    public ResponseEntity<?> existsByPhone(@PathVariable String phone) {
        return new ResponseEntity<>(this.USER_SERVICE_IMPL.existsByPhone(phone), HttpStatus.OK);
    }

    // ================ TRACK USER ACTIVITY ===================== //
    @PostMapping("/{identifier}/track-activity")
    public ResponseEntity<?> trackActivity(@PathVariable String identifier, @RequestBody UserActivityTrackRequest request, HttpServletRequest httpRequest) {
        User user = this.USER_SERVICE_IMPL.findById(identifier)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (request.getCity() != null && !request.getCity().isBlank()) {
            if (user.getSearchedCities() == null) {
                user.setSearchedCities(new ArrayList<>());
            }
            if (!user.getSearchedCities().contains(request.getCity())) {
                user.getSearchedCities().add(request.getCity());
            }
        }

        if (request.getExam() != null && !request.getExam().isBlank()) {
            if (user.getSearchedExams() == null) {
                user.setSearchedExams(new ArrayList<>());
            }
            if (!user.getSearchedExams().contains(request.getExam())) {
                user.getSearchedExams().add(request.getExam());
            }
        }

        if (request.getInstituteIdentifier() != null && !request.getInstituteIdentifier().isBlank()) {
            if (user.getVisitedInstituteIdentifiers() == null) {
                user.setVisitedInstituteIdentifiers(new ArrayList<>());
                user.setVisitedInstituteNames(new ArrayList<>());
            }
            if (!user.getVisitedInstituteIdentifiers().contains(request.getInstituteIdentifier())) {
                user.getVisitedInstituteIdentifiers().add(request.getInstituteIdentifier());
                user.getVisitedInstituteNames().add(request.getInstituteName() != null ? request.getInstituteName() : "Unknown Institute");
            }
        }

        User updated = this.USER_SERVICE_IMPL.save(user);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(httpRequest);
        if (actor.isAuthenticated()) {
            boolean searched = (request.getCity() != null && !request.getCity().isBlank())
                    || (request.getExam() != null && !request.getExam().isBlank());
            boolean viewed = request.getInstituteIdentifier() != null && !request.getInstituteIdentifier().isBlank();

            if (searched) {
                ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                        .actorType(actor.getType())
                        .actorIdentifier(actor.getIdentifier())
                        .actorName(actor.getName())
                        .actionType(ActivityActionType.SEARCHED_INSTITUTES)
                        .entityType(ActivityEntityType.INSTITUTE)
                        .description("Searched institutes")
                        .metadata(Map.of(
                                "city", request.getCity() != null ? request.getCity() : "",
                                "exam", request.getExam() != null ? request.getExam() : ""
                        ))
                        .ipAddress(httpRequest.getRemoteAddr())
                        .userAgent(httpRequest.getHeader("User-Agent"))
                        .source("FRONTEND")
                        .build());
            }

            if (viewed) {
                ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                        .actorType(actor.getType())
                        .actorIdentifier(actor.getIdentifier())
                        .actorName(actor.getName())
                        .actionType(ActivityActionType.VIEWED_INSTITUTE)
                        .entityType(ActivityEntityType.INSTITUTE)
                        .entityIdentifier(request.getInstituteIdentifier())
                        .entityName(request.getInstituteName())
                        .instituteIdentifier(request.getInstituteIdentifier())
                        .description("Viewed institute" + (request.getInstituteName() != null ? " " + request.getInstituteName() : ""))
                        .ipAddress(httpRequest.getRemoteAddr())
                        .userAgent(httpRequest.getHeader("User-Agent"))
                        .source("FRONTEND")
                        .build());
            }
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
