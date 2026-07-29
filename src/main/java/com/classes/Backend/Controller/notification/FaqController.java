package com.classes.Backend.Controller.notification;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.notification.Faq;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.notification.FaqServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
public class FaqController {

    private final FaqServiceImpl FAQ_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE FAQ ===================== //
    @PostMapping
    public ResponseEntity<?> saveFaq(@RequestBody Faq faq, HttpServletRequest request) {
        Faq saved = this.FAQ_SERVICE_IMPL.save(faq);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FAQ_CREATED)
                    .entityType(ActivityEntityType.FAQ)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getQuestion())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created FAQ" + (saved.getQuestion() != null ? ": " + truncate(saved.getQuestion(), 80) : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL FAQS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllFaqs(@RequestBody List<Faq> faqs) {
        return new ResponseEntity<>(this.FAQ_SERVICE_IMPL.saveAll(faqs), HttpStatus.CREATED);
    }

    // ================ GET FAQ BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getFaqById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.FAQ_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL FAQS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllFaqs() {
        List<Faq> allFaqs = this.FAQ_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allFaqs, HttpStatus.OK);
    }

    // ================ DELETE FAQ BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteFaqById(@PathVariable String identifier, HttpServletRequest request) {
        Faq existing = this.FAQ_SERVICE_IMPL.findById(identifier).orElse(null);

        this.FAQ_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FAQ_DELETED)
                    .entityType(ActivityEntityType.FAQ)
                    .entityIdentifier(identifier)
                    .entityName(existing.getQuestion())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted FAQ" + (existing.getQuestion() != null ? ": " + truncate(existing.getQuestion(), 80) : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("Faq deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE FAQ BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateFaqById(@PathVariable String identifier, @RequestBody Faq faq, HttpServletRequest request) {
        Faq existing = this.FAQ_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("Faq not found", HttpStatus.NOT_FOUND);
        }
        faq.setIdentifier(identifier);
        Faq updated = this.FAQ_SERVICE_IMPL.save(faq);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FAQ_UPDATED)
                    .entityType(ActivityEntityType.FAQ)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getQuestion())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated FAQ" + (updated.getQuestion() != null ? ": " + truncate(updated.getQuestion(), 80) : ""))
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) + "..." : value;
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.FAQ_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND ACTIVE FAQS ===================== //
    @GetMapping("/institute/{instituteIdentifier}/active")
    public ResponseEntity<?> findByInstituteIdentifierAndIsActiveTrue(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.FAQ_SERVICE_IMPL.findByInstituteIdentifierAndIsActiveTrue(instituteIdentifier), HttpStatus.OK);
    }
}
