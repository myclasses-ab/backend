package com.classes.Backend.Service.media;

import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import com.classes.Backend.Repository.media.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {
    private final MediaRepository MEDIA_REPOSITORY;

    // ================ SAVE MEDIA ===================== //
    @Override
    public Media save(Media media) {
        return this.MEDIA_REPOSITORY.save(media);
    }

    // ================ SAVE ALL MEDIAS ===================== //
    @Override
    public List<Media> saveAll(List<Media> medias) {
        return this.MEDIA_REPOSITORY.saveAll(medias);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Media> findById(String identifier) {
        return this.MEDIA_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Media> findAll() {
        return this.MEDIA_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.MEDIA_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Media with identifier '" + identifier + "' not found");
        }
        this.MEDIA_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.MEDIA_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Media> findByInstituteIdentifier(String instituteIdentifier) {
        return this.MEDIA_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @Override
    public List<Media> findByBranchIdentifier(String branchIdentifier) {
        return this.MEDIA_REPOSITORY.findByBranchIdentifier(branchIdentifier);
    }

    // ================ FIND BY ENTITY TYPE ===================== //
    @Override
    public List<Media> findByEntityType(MediaEntityType entityType) {
        return this.MEDIA_REPOSITORY.findByEntityType(entityType);
    }

    // ================ FIND BY MEDIA TYPE ===================== //
    @Override
    public List<Media> findByMediaType(MediaType mediaType) {
        return this.MEDIA_REPOSITORY.findByMediaType(mediaType);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER AND ENTITY TYPE ===================== //
    @Override
    public List<Media> findByInstituteIdentifierAndEntityType(String instituteIdentifier, MediaEntityType entityType) {
        return this.MEDIA_REPOSITORY.findByInstituteIdentifierAndEntityType(instituteIdentifier, entityType);
    }

    // ================ FIND BY IS FEATURED TRUE ===================== //
    @Override
    public List<Media> findByIsFeaturedTrue() {
        return this.MEDIA_REPOSITORY.findByIsFeaturedTrue();
    }
}
