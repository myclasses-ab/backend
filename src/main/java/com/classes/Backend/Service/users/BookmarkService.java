package com.classes.Backend.Service.users;

import com.classes.Backend.Domain.users.Bookmark;
import com.classes.Backend.Domain.enums.BookmarkEntityType;

import java.util.List;
import java.util.Optional;

public interface BookmarkService {
    // ================ CRUD OPERATIONS ===================== //
    Bookmark save(Bookmark bookmark);
    List<Bookmark> saveAll(List<Bookmark> bookmarks);
    Optional<Bookmark> findById(String identifier);
    List<Bookmark> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Bookmark> findByUserIdentifier(String userIdentifier);
    List<Bookmark> findByUserIdentifierAndEntityType(String userIdentifier, BookmarkEntityType entityType);
    Optional<Bookmark> findByUserIdentifierAndEntityTypeAndEntityIdentifier(String userIdentifier, BookmarkEntityType entityType, String entityIdentifier);
    boolean existsByUserIdentifierAndEntityTypeAndEntityIdentifier(String userIdentifier, BookmarkEntityType entityType, String entityIdentifier);
}
