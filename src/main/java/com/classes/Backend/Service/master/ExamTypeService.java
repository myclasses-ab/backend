package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.ExamType;

import java.util.List;
import java.util.Optional;

public interface ExamTypeService {
    // ================ CRUD OPERATIONS ===================== //
    ExamType save(ExamType examType);
    List<ExamType> saveAll(List<ExamType> examTypes);
    Optional<ExamType> findById(String identifier);
    List<ExamType> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<ExamType> findBySlug(String slug);
    List<ExamType> findByStreamIdentifier(String streamIdentifier);
}
