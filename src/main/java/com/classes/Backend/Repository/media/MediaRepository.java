package com.classes.Backend.Repository.media;

import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, String> {
    List<Media> findByInstituteIdentifier(String instituteIdentifier);
    List<Media> findByBranchIdentifier(String branchIdentifier);
    List<Media> findByEntityType(MediaEntityType entityType);
    List<Media> findByMediaType(MediaType mediaType);
    List<Media> findByInstituteIdentifierAndEntityType(String instituteIdentifier, MediaEntityType entityType);
    List<Media> findByIsFeaturedTrue();
}
