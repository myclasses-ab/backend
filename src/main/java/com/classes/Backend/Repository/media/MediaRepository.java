package com.classes.Backend.Repository.media;

import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, String> {
    List<Media> findByInstituteIdentifier(String instituteIdentifier);
    List<Media> findByBranchIdentifier(String branchIdentifier);
    List<Media> findByEntityType(MediaEntityType entityType);
    List<Media> findByMediaType(MediaType mediaType);
    List<Media> findByInstituteIdentifierAndEntityType(String instituteIdentifier, MediaEntityType entityType);
    List<Media> findByInstituteIdentifierAndEntityTypeAndIsFeaturedTrue(String instituteIdentifier, MediaEntityType entityType);

    @Query("SELECT m FROM Media m WHERE m.instituteIdentifier IN :instituteIdentifiers AND m.entityType = :entityType AND m.isFeatured = true AND m.mediaType = :mediaType ORDER BY m.displayOrder ASC, m.createdAt ASC")
    List<Media> findStarredImagesByInstituteIdentifiers(
            @Param("instituteIdentifiers") List<String> instituteIdentifiers,
            @Param("entityType") MediaEntityType entityType,
            @Param("mediaType") MediaType mediaType
    );

    List<Media> findByIsFeaturedTrue();
}
