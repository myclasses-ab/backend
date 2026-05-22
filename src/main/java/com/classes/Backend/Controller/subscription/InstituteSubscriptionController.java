package com.classes.Backend.Controller.subscription;

import com.classes.Backend.Domain.subscription.InstituteSubscription;
import com.classes.Backend.Service.subscription.InstituteSubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-subscriptions")
public class InstituteSubscriptionController {

    private final InstituteSubscriptionServiceImpl INSTITUTE_SUBSCRIPTION_SERVICE_IMPL;

    // ================ CREATE INSTITUTE SUBSCRIPTION ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteSubscription(@RequestBody InstituteSubscription instituteSubscription) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.save(instituteSubscription), HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTE SUBSCRIPTIONS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstituteSubscriptions(@RequestBody List<InstituteSubscription> instituteSubscriptions) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.saveAll(instituteSubscriptions), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE SUBSCRIPTION BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteSubscriptionById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTE SUBSCRIPTIONS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstituteSubscriptions() {
        List<InstituteSubscription> allSubscriptions = this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allSubscriptions, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE SUBSCRIPTION BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteSubscriptionById(@PathVariable String identifier) {
        this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("InstituteSubscription deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTESUBSCRIPTION BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteSubscriptionById(@PathVariable String identifier, @RequestBody InstituteSubscription instituteSubscription) {
        if (!this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InstituteSubscription not found", HttpStatus.NOT_FOUND);
        }
        instituteSubscription.setIdentifier(identifier);
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.save(instituteSubscription), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY PLAN IDENTIFIER ===================== //
    @GetMapping("/plan/{planIdentifier}")
    public ResponseEntity<?> findByPlanIdentifier(@PathVariable String planIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findByPlanIdentifier(planIdentifier), HttpStatus.OK);
    }

    // ================ FIND ACTIVE SUBSCRIPTIONS ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }
}
