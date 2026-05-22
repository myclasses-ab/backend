package com.classes.Backend.Domain.media;

import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "media")
public class Media {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "branch_identifier")
    private String branchIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private MediaEntityType entityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType mediaType;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @Column(name = "caption", length = 500)
    private String caption;

    @Column(name = "alt_text", length = 300)
    private String altText;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "file_size_kb")
    private Integer fileSizeKb;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
