package com.classes.Backend.Service.media;

import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    // ================ CRUD OPERATIONS ===================== //
    Media save(Media media);
    List<Media> saveAll(List<Media> mediaList);
    Optional<Media> findById(String identifier);
    List<Media> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Media> findByInstituteIdentifier(String instituteIdentifier);
    List<Media> findByBranchIdentifier(String branchIdentifier);
    List<Media> findByEntityType(MediaEntityType entityType);
    List<Media> findByMediaType(MediaType mediaType);
    List<Media> findByInstituteIdentifierAndEntityType(String instituteIdentifier, MediaEntityType entityType);
    List<Media> findByIsFeaturedTrue();
}
