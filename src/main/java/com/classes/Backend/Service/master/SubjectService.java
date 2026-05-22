package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    // ================ CRUD OPERATIONS ===================== //
    Subject save(Subject subject);
    List<Subject> saveAll(List<Subject> subjects);
    Optional<Subject> findById(String identifier);
    List<Subject> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<Subject> findBySlug(String slug);
    List<Subject> findByStreamIdentifier(String streamIdentifier);
}
