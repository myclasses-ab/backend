package com.classes.Backend.Repository.notification;

import com.classes.Backend.Domain.notification.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, String> {
    List<Faq> findByInstituteIdentifier(String instituteIdentifier);
    List<Faq> findByInstituteIdentifierAndIsActiveTrue(String instituteIdentifier);
}
