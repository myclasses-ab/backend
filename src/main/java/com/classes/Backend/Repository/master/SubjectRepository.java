package com.classes.Backend.Repository.master;

import com.classes.Backend.Domain.master.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    Optional<Subject> findBySlug(String slug);
    List<Subject> findByStreamIdentifier(String streamIdentifier);
}
