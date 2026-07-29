package com.classes.Backend.Controller.users;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.BookmarkEntityType;
import com.classes.Backend.Domain.users.Bookmark;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.users.BookmarkServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkServiceImpl BOOKMARK_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE BOOKMARK ===================== //
    @PostMapping
    public ResponseEntity<?> saveBookmark(@RequestBody Bookmark bookmark, HttpServletRequest request) {
        Bookmark saved = this.BOOKMARK_SERVICE_IMPL.save(bookmark);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(ActivityActorType.STUDENT)
                    .actorIdentifier(saved.getUserIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.BOOKMARKED)
                    .entityType(ActivityEntityType.BOOKMARK)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getEntityType() + ":" + saved.getEntityIdentifier())
                    .description("Bookmarked " + saved.getEntityType().name().toLowerCase())
                    .metadata(Map.of(
                            "entityType", saved.getEntityType().name(),
                            "entityIdentifier", saved.getEntityIdentifier()
                    ))
                    .source("FRONTEND")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteBookmarkById(@PathVariable String identifier, HttpServletRequest request) {
        Bookmark existing = this.BOOKMARK_SERVICE_IMPL.findById(identifier).orElse(null);

        this.BOOKMARK_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(ActivityActorType.STUDENT)
                    .actorIdentifier(existing.getUserIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.REMOVED_BOOKMARK)
                    .entityType(ActivityEntityType.BOOKMARK)
                    .entityIdentifier(identifier)
                    .entityName(existing.getEntityType() + ":" + existing.getEntityIdentifier())
                    .description("Removed bookmark for " + existing.getEntityType().name().toLowerCase())
                    .metadata(Map.of(
                            "entityType", existing.getEntityType().name(),
                            "entityIdentifier", existing.getEntityIdentifier()
                    ))
                    .source("FRONTEND")
                    .build());
        }

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
