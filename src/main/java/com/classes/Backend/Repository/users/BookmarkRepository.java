package com.classes.Backend.Repository.users;

import com.classes.Backend.Domain.users.Bookmark;
import com.classes.Backend.Domain.enums.BookmarkEntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {
    List<Bookmark> findByUserIdentifier(String userIdentifier);
    List<Bookmark> findByUserIdentifierAndEntityType(String userIdentifier, BookmarkEntityType entityType);
    Optional<Bookmark> findByUserIdentifierAndEntityTypeAndEntityIdentifier(String userIdentifier, BookmarkEntityType entityType, String entityIdentifier);
    boolean existsByUserIdentifierAndEntityTypeAndEntityIdentifier(String userIdentifier, BookmarkEntityType entityType, String entityIdentifier);
}
