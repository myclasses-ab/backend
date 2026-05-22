package com.classes.Backend.Service.notification;

import com.classes.Backend.Domain.notification.Faq;

import java.util.List;
import java.util.Optional;

public interface FaqService {
    // ================ CRUD OPERATIONS ===================== //
    Faq save(Faq faq);
    List<Faq> saveAll(List<Faq> faqs);
    Optional<Faq> findById(String identifier);
    List<Faq> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Faq> findByInstituteIdentifier(String instituteIdentifier);
    List<Faq> findByInstituteIdentifierAndIsActiveTrue(String instituteIdentifier);
}
