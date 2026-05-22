package com.classes.Backend.Controller.notification;

import com.classes.Backend.Domain.notification.Faq;
import com.classes.Backend.Service.notification.FaqServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
public class FaqController {

    private final FaqServiceImpl FAQ_SERVICE_IMPL;

    // ================ CREATE FAQ ===================== //
    @PostMapping
    public ResponseEntity<?> saveFaq(@RequestBody Faq faq) {
        return new ResponseEntity<>(this.FAQ_SERVICE_IMPL.save(faq), HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteFaqById(@PathVariable String identifier) {
        this.FAQ_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Faq deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE FAQ BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateFaqById(@PathVariable String identifier, @RequestBody Faq faq) {
        if (!this.FAQ_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Faq not found", HttpStatus.NOT_FOUND);
        }
        faq.setIdentifier(identifier);
        return new ResponseEntity<>(this.FAQ_SERVICE_IMPL.save(faq), HttpStatus.OK);
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
