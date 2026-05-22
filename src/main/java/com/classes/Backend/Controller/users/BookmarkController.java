package com.classes.Backend.Controller.users;

import com.classes.Backend.Domain.enums.BookmarkEntityType;
import com.classes.Backend.Domain.users.Bookmark;
import com.classes.Backend.Service.users.BookmarkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkServiceImpl BOOKMARK_SERVICE_IMPL;

    // ================ CREATE BOOKMARK ===================== //
    @PostMapping
    public ResponseEntity<?> saveBookmark(@RequestBody Bookmark bookmark) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.save(bookmark), HttpStatus.CREATED);
    }

    // ================ CREATE ALL BOOKMARKS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllBookmarks(@RequestBody List<Bookmark> bookmarks) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.saveAll(bookmarks), HttpStatus.CREATED);
    }

    // ================ GET BOOKMARK BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getBookmarkById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL BOOKMARKS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllBookmarks() {
        List<Bookmark> allBookmarks = this.BOOKMARK_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allBookmarks, HttpStatus.OK);
    }

    // ================ DELETE BOOKMARK BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteBookmarkById(@PathVariable String identifier) {
        this.BOOKMARK_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Bookmark deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE BOOKMARK BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateBookmarkById(@PathVariable String identifier, @RequestBody Bookmark bookmark) {
        if (!this.BOOKMARK_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Bookmark not found", HttpStatus.NOT_FOUND);
        }
        bookmark.setIdentifier(identifier);
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.save(bookmark), HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY USER AND ENTITY TYPE ===================== //
    @GetMapping("/user/{userIdentifier}/entity-type/{entityType}")
    public ResponseEntity<?> findByUserIdentifierAndEntityType(@PathVariable String userIdentifier, @PathVariable BookmarkEntityType entityType) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.findByUserIdentifierAndEntityType(userIdentifier, entityType), HttpStatus.OK);
    }

    // ================ FIND SPECIFIC BOOKMARK ===================== //
    @GetMapping("/user/{userIdentifier}/entity-type/{entityType}/entity/{entityIdentifier}")
    public ResponseEntity<?> findByUserIdentifierAndEntityTypeAndEntityIdentifier(@PathVariable String userIdentifier, @PathVariable BookmarkEntityType entityType, @PathVariable String entityIdentifier) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.findByUserIdentifierAndEntityTypeAndEntityIdentifier(userIdentifier, entityType, entityIdentifier), HttpStatus.OK);
    }

    // ================ CHECK BOOKMARK EXISTS ===================== //
    @GetMapping("/exists/user/{userIdentifier}/entity-type/{entityType}/entity/{entityIdentifier}")
    public ResponseEntity<?> existsByUserIdentifierAndEntityTypeAndEntityIdentifier(@PathVariable String userIdentifier, @PathVariable BookmarkEntityType entityType, @PathVariable String entityIdentifier) {
        return new ResponseEntity<>(this.BOOKMARK_SERVICE_IMPL.existsByUserIdentifierAndEntityTypeAndEntityIdentifier(userIdentifier, entityType, entityIdentifier), HttpStatus.OK);
    }
}
