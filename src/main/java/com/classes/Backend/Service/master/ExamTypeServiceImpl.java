package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.ExamType;
import com.classes.Backend.Repository.master.ExamTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExamTypeServiceImpl implements ExamTypeService {
    private final ExamTypeRepository EXAM_TYPE_REPOSITORY;

    // ================ SAVE EXAM TYPE ===================== //
    @Override
    public ExamType save(ExamType examType) {
        return this.EXAM_TYPE_REPOSITORY.save(examType);
    }

    // ================ SAVE ALL EXAM TYPES ===================== //
    @Override
    public List<ExamType> saveAll(List<ExamType> examTypes) {
        return this.EXAM_TYPE_REPOSITORY.saveAll(examTypes);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<ExamType> findById(String identifier) {
        return this.EXAM_TYPE_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<ExamType> findAll() {
        return this.EXAM_TYPE_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.EXAM_TYPE_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("ExamType with identifier '" + identifier + "' not found");
        }
        this.EXAM_TYPE_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.EXAM_TYPE_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY SLUG ===================== //
    @Override
    public Optional<ExamType> findBySlug(String slug) {
        return this.EXAM_TYPE_REPOSITORY.findBySlug(slug);
    }

    // ================ FIND BY STREAM IDENTIFIER ===================== //
    @Override
    public List<ExamType> findByStreamIdentifier(String streamIdentifier) {
        return this.EXAM_TYPE_REPOSITORY.findByStreamIdentifier(streamIdentifier);
    }
}
