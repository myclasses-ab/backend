package com.classes.Backend.Service.results;

import com.classes.Backend.Domain.results.Result;
import com.classes.Backend.Repository.results.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository RESULT_REPOSITORY;

    // ================ SAVE RESULT ===================== //
    @Override
    public Result save(Result result) {
        return this.RESULT_REPOSITORY.save(result);
    }

    // ================ SAVE ALL RESULTS ===================== //
    @Override
    public List<Result> saveAll(List<Result> results) {
        return this.RESULT_REPOSITORY.saveAll(results);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Result> findById(String identifier) {
        return this.RESULT_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Result> findAll() {
        return this.RESULT_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.RESULT_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Result with identifier '" + identifier + "' not found");
        }
        this.RESULT_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.RESULT_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Result> findByInstituteIdentifier(String instituteIdentifier) {
        return this.RESULT_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY IS FEATURED TRUE ===================== //
    @Override
    public List<Result> findByIsFeaturedTrue() {
        return this.RESULT_REPOSITORY.findByIsFeaturedTrue();
    }
}
