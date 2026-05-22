package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.Stream;

import java.util.List;
import java.util.Optional;

public interface StreamService {
    // ================ CRUD OPERATIONS ===================== //
    Stream save(Stream stream);
    List<Stream> saveAll(List<Stream> streams);
    Optional<Stream> findById(String identifier);
    List<Stream> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<Stream> findBySlug(String slug);
}
