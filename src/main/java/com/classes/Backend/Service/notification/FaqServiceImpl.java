package com.classes.Backend.Service.notification;

import com.classes.Backend.Domain.notification.Faq;
import com.classes.Backend.Repository.notification.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FaqServiceImpl implements FaqService {
    private final FaqRepository FAQ_REPOSITORY;

    // ================ SAVE FAQ ===================== //
    @Override
    public Faq save(Faq faq) {
        return this.FAQ_REPOSITORY.save(faq);
    }

    // ================ SAVE ALL FAQS ===================== //
    @Override
    public List<Faq> saveAll(List<Faq> faqs) {
        return this.FAQ_REPOSITORY.saveAll(faqs);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Faq> findById(String identifier) {
        return this.FAQ_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Faq> findAll() {
        return this.FAQ_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.FAQ_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Faq with identifier '" + identifier + "' not found");
        }
        this.FAQ_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.FAQ_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Faq> findByInstituteIdentifier(String instituteIdentifier) {
        return this.FAQ_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER AND IS ACTIVE TRUE ===================== //
    @Override
    public List<Faq> findByInstituteIdentifierAndIsActiveTrue(String instituteIdentifier) {
        return this.FAQ_REPOSITORY.findByInstituteIdentifierAndIsActiveTrue(instituteIdentifier);
    }
}
