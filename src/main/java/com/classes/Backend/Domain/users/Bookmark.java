package com.classes.Backend.Domain.users;

import com.classes.Backend.Domain.enums.BookmarkEntityType;
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
@Table(name = "bookmarks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_identifier", "entity_type", "entity_identifier"})
})
public class Bookmark {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private BookmarkEntityType entityType;

    @Column(name = "entity_identifier")
    private String entityIdentifier;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
