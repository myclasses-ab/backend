package com.classes.Backend.Service.results;

import com.classes.Backend.Domain.results.Result;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    // ================ CRUD OPERATIONS ===================== //
    Result save(Result result);
    List<Result> saveAll(List<Result> results);
    Optional<Result> findById(String identifier);
    List<Result> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Result> findByInstituteIdentifier(String instituteIdentifier);
    List<Result> findByIsFeaturedTrue();
}
