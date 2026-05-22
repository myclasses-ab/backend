package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.Subject;
import com.classes.Backend.Repository.master.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository SUBJECT_REPOSITORY;

    // ================ SAVE SUBJECT ===================== //
    @Override
    public Subject save(Subject subject) {
        return this.SUBJECT_REPOSITORY.save(subject);
    }

    // ================ SAVE ALL SUBJECTS ===================== //
    @Override
    public List<Subject> saveAll(List<Subject> subjects) {
        return this.SUBJECT_REPOSITORY.saveAll(subjects);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Subject> findById(String identifier) {
        return this.SUBJECT_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Subject> findAll() {
        return this.SUBJECT_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.SUBJECT_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Subject with identifier '" + identifier + "' not found");
        }
        this.SUBJECT_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.SUBJECT_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY SLUG ===================== //
    @Override
    public Optional<Subject> findBySlug(String slug) {
        return this.SUBJECT_REPOSITORY.findBySlug(slug);
    }

    // ================ FIND BY STREAM IDENTIFIER ===================== //
    @Override
    public List<Subject> findByStreamIdentifier(String streamIdentifier) {
        return this.SUBJECT_REPOSITORY.findByStreamIdentifier(streamIdentifier);
    }
}
