package com.classes.Backend.Controller.leads;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import com.classes.Backend.Domain.leads.Inquiry;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.auth.JwtService;
import com.classes.Backend.Service.leads.InquiryServiceImpl;
import com.classes.Backend.Service.users.UserService;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.leads.InstituteInquiryResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryServiceImpl INQUIRY_SERVICE_IMPL;
    private final JwtService JWT_SERVICE;
    private final UserService USER_SERVICE;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE INQUIRY ===================== //
    @PostMapping
    public ResponseEntity<?> saveInquiry(@RequestBody Inquiry inquiry,
                                          @RequestHeader(value = "Authorization", required = false) String authHeader,
                                          HttpServletRequest request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String username = JWT_SERVICE.extractUsername(token);
                User user = USER_SERVICE.findByEmail(username)
                        .orElseGet(() -> USER_SERVICE.findByPhone(username).orElse(null));
                if (user != null) {
                    if (inquiry.getUserIdentifier() == null || inquiry.getUserIdentifier().isBlank()) {
                        inquiry.setUserIdentifier(user.getIdentifier());
                    }
                    if (inquiry.getName() == null || inquiry.getName().isBlank()) {
                        inquiry.setName(user.getFullName());
                    }
                    if (inquiry.getPhone() == null || inquiry.getPhone().isBlank()) {
                        inquiry.setPhone(user.getPhone());
                    }
                    if (inquiry.getEmail() == null || inquiry.getEmail().isBlank()) {
                        inquiry.setEmail(user.getEmail());
                    }
                }
            } catch (Exception e) {
                // Ignore auth errors for anonymous tracking
            }
        }
        Inquiry saved = this.INQUIRY_SERVICE_IMPL.save(inquiry);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        ActivityActionType action = saved.getSource() == InquirySource.CALLBACK_REQUEST
                ? ActivityActionType.BOOKED_DEMO
                : ActivityActionType.SUBMITTED_INQUIRY;
        String actorIdentifier = actor.isAuthenticated() ? actor.getIdentifier() : saved.getUserIdentifier();
        String actorName = actor.isAuthenticated() ? actor.getName() : saved.getName();
        String instituteName = saved.getInstituteIdentifier();

        ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                .actorType(ActivityActorType.STUDENT)
                .actorIdentifier(actorIdentifier)
                .actorName(actorName)
                .actionType(action)
                .entityType(ActivityEntityType.INQUIRY)
                .entityIdentifier(saved.getIdentifier())
                .entityName(saved.getName())
                .instituteIdentifier(saved.getInstituteIdentifier())
                .description((action == ActivityActionType.BOOKED_DEMO ? "Booked a demo" : "Submitted inquiry")
                        + (instituteName != null ? " for institute" : ""))
                .metadata(Map.of(
                        "studentName", saved.getName() != null ? saved.getName() : "",
                        "phone", maskPhone(saved.getPhone()),
                        "targetExam", saved.getTargetExam() != null ? saved.getTargetExam() : "",
                        "standard", saved.getStandard() != null ? saved.getStandard() : "",
                        "source", saved.getSource() != null ? saved.getSource().name() : ""
                ))
                .source("FRONTEND")
                .build());

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        return "****" + phone.substring(phone.length() - 4);
    }

    // ================ CREATE ALL INQUIRIES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInquiries(@RequestBody List<Inquiry> inquiries) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.saveAll(inquiries), HttpStatus.CREATED);
    }

    // ================ GET INQUIRY BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInquiryById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INQUIRIES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInquiries() {
        List<Inquiry> allInquiries = this.INQUIRY_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allInquiries, HttpStatus.OK);
    }

    // ================ DELETE INQUIRY BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInquiryById(@PathVariable String identifier) {
        this.INQUIRY_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Inquiry deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INQUIRYSOURCE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInquiryById(@PathVariable String identifier, @RequestBody Inquiry inquiry) {
        if (!this.INQUIRY_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InquirySource not found", HttpStatus.NOT_FOUND);
        }
        inquiry.setIdentifier(identifier);
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.save(inquiry), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        List<InstituteInquiryResponse> responses = this.INQUIRY_SERVICE_IMPL.findInstituteInquiryResponses(instituteIdentifier);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // ================ UNLOCK INQUIRY CONTACT ===================== //
    @PostMapping("/{identifier}/unlock")
    public ResponseEntity<?> unlockInquiry(@PathVariable String identifier,
                                            @RequestBody Map<String, String> request,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(Map.of("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }

        String instituteIdentifier = request.get("instituteIdentifier");
        if (instituteIdentifier == null || instituteIdentifier.isBlank()) {
            return new ResponseEntity<>(Map.of("error", "instituteIdentifier is required"), HttpStatus.BAD_REQUEST);
        }

        try {
            String token = authHeader.substring(7);
            String username = JWT_SERVICE.extractUsername(token);
            User user = USER_SERVICE.findByEmail(username)
                    .orElseGet(() -> USER_SERVICE.findByPhone(username).orElse(null));
            if (user == null) {
                return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.UNAUTHORIZED);
            }

            InstituteInquiryResponse response = this.INQUIRY_SERVICE_IMPL.unlockInquiry(identifier, instituteIdentifier, user.getIdentifier());

            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(ActivityActorType.INSTITUTE_ADMIN)
                    .actorIdentifier(user.getIdentifier())
                    .actorName(user.getFullName())
                    .actionType(ActivityActionType.UNLOCKED_LEAD)
                    .entityType(ActivityEntityType.INQUIRY)
                    .entityIdentifier(identifier)
                    .instituteIdentifier(instituteIdentifier)
                    .description("Unlocked lead contact")
                    .source("CONSOLE")
                    .build());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @GetMapping("/branch/{branchIdentifier}")
    public ResponseEntity<?> findByBranchIdentifier(@PathVariable String branchIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByBranchIdentifier(branchIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY COURSE IDENTIFIER ===================== //
    @GetMapping("/course/{courseIdentifier}")
    public ResponseEntity<?> findByCourseIdentifier(@PathVariable String courseIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByCourseIdentifier(courseIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY STATUS ===================== //
    @GetMapping("/status/{status}")
    public ResponseEntity<?> findByStatus(@PathVariable InquiryStatus status) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByStatus(status), HttpStatus.OK);
    }

    // ================ FIND BY SOURCE ===================== //
    @GetMapping("/source/{source}")
    public ResponseEntity<?> findBySource(@PathVariable InquirySource source) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findBySource(source), HttpStatus.OK);
    }

    // ================ FIND BY ASSIGNED TO ===================== //
    @GetMapping("/assigned-to/{assignedTo}")
    public ResponseEntity<?> findByAssignedTo(@PathVariable String assignedTo) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByAssignedTo(assignedTo), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE AND STATUS ===================== //
    @GetMapping("/institute/{instituteIdentifier}/status/{status}")
    public ResponseEntity<?> findByInstituteIdentifierAndStatus(@PathVariable String instituteIdentifier, @PathVariable InquiryStatus status) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByInstituteIdentifierAndStatus(instituteIdentifier, status), HttpStatus.OK);
    }
}
