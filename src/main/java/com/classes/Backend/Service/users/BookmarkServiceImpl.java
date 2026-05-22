package com.classes.Backend.Service.users;

import com.classes.Backend.Domain.users.Bookmark;
import com.classes.Backend.Domain.enums.BookmarkEntityType;
import com.classes.Backend.Repository.users.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository BOOKMARK_REPOSITORY;

    // ================ SAVE BOOKMARK ===================== //
    @Override
    public Bookmark save(Bookmark bookmark) {
        return this.BOOKMARK_REPOSITORY.save(bookmark);
    }

    // ================ SAVE ALL BOOKMARKS ===================== //
    @Override
    public List<Bookmark> saveAll(List<Bookmark> bookmarks) {
        return this.BOOKMARK_REPOSITORY.saveAll(bookmarks);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Bookmark> findById(String identifier) {
        return this.BOOKMARK_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Bookmark> findAll() {
        return this.BOOKMARK_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.BOOKMARK_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Bookmark with identifier '" + identifier + "' not found");
        }
        this.BOOKMARK_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.BOOKMARK_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @Override
    public List<Bookmark> findByUserIdentifier(String userIdentifier) {
        return this.BOOKMARK_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    // ================ FIND BY USER IDENTIFIER AND ENTITY TYPE ===================== //
    @Override
    public List<Bookmark> findByUserIdentifierAndEntityType(String userIdentifier, BookmarkEntityType entityType) {
        return this.BOOKMARK_REPOSITORY.findByUserIdentifierAndEntityType(userIdentifier, entityType);
    }

    // ================ FIND BY USER IDENTIFIER AND ENTITY TYPE AND ENTITY IDENTIFIER ===================== //
    @Override
    public Optional<Bookmark> findByUserIdentifierAndEntityTypeAndEntityIdentifier(String userIdentifier, BookmarkEntityType entityType, String entityIdentifier) {
        return this.BOOKMARK_REPOSITORY.findByUserIdentifierAndEntityTypeAndEntityIdentifier(userIdentifier, entityType, entityIdentifier);
    }

    // ================ EXISTS BY USER IDENTIFIER AND ENTITY TYPE AND ENTITY IDENTIFIER ===================== //
    @Override
    public boolean existsByUserIdentifierAndEntityTypeAndEntityIdentifier(String userIdentifier, BookmarkEntityType entityType, String entityIdentifier) {
        return this.BOOKMARK_REPOSITORY.existsByUserIdentifierAndEntityTypeAndEntityIdentifier(userIdentifier, entityType, entityIdentifier);
    }
}
