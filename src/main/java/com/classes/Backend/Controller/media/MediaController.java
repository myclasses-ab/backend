package com.classes.Backend.Controller.media;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.media.MediaServiceImpl;
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
@RequestMapping("/api/media")
public class MediaController {

    private final MediaServiceImpl MEDIA_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE MEDIA ===================== //
    @PostMapping
    public ResponseEntity<?> saveMedia(@RequestBody Media media, HttpServletRequest request) {
        Media saved = this.MEDIA_SERVICE_IMPL.save(media);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.MEDIA_UPLOADED)
                    .entityType(ActivityEntityType.MEDIA)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getCaption() != null ? saved.getCaption() : saved.getUrl())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Uploaded " + (saved.getMediaType() != null ? saved.getMediaType().name() : "media"))
                    .metadata(Map.of(
                            "mediaType", saved.getMediaType() != null ? saved.getMediaType().name() : null,
                            "entityType", saved.getEntityType() != null ? saved.getEntityType().name() : null
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL MEDIA ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllMedia(@RequestBody List<Media> mediaList) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.saveAll(mediaList), HttpStatus.CREATED);
    }

    // ================ GET MEDIA BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getMediaById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL MEDIA ===================== //
    @GetMapping
    public ResponseEntity<?> getAllMedia() {
        List<Media> allMedia = this.MEDIA_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allMedia, HttpStatus.OK);
    }

    // ================ DELETE MEDIA BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteMediaById(@PathVariable String identifier, HttpServletRequest request) {
        Media existing = this.MEDIA_SERVICE_IMPL.findById(identifier).orElse(null);

        this.MEDIA_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.MEDIA_DELETED)
                    .entityType(ActivityEntityType.MEDIA)
                    .entityIdentifier(identifier)
                    .entityName(existing.getCaption() != null ? existing.getCaption() : existing.getUrl())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted " + (existing.getMediaType() != null ? existing.getMediaType().name() : "media"))
                    .metadata(Map.of(
                            "mediaType", existing.getMediaType() != null ? existing.getMediaType().name() : null,
                            "entityType", existing.getEntityType() != null ? existing.getEntityType().name() : null
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("Media deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE MEDIAENTITYTYPE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateMediaById(@PathVariable String identifier, @RequestBody Media media, HttpServletRequest request) {
        if (!this.MEDIA_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("MediaEntityType not found", HttpStatus.NOT_FOUND);
        }
        media.setIdentifier(identifier);
        Media updated = this.MEDIA_SERVICE_IMPL.save(media);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.MEDIA_UPLOADED)
                    .entityType(ActivityEntityType.MEDIA)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getCaption() != null ? updated.getCaption() : updated.getUrl())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated " + (updated.getMediaType() != null ? updated.getMediaType().name() : "media"))
                    .metadata(Map.of(
                            "mediaType", updated.getMediaType() != null ? updated.getMediaType().name() : null,
                            "entityType", updated.getEntityType() != null ? updated.getEntityType().name() : null
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @GetMapping("/branch/{branchIdentifier}")
    public ResponseEntity<?> findByBranchIdentifier(@PathVariable String branchIdentifier) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findByBranchIdentifier(branchIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY ENTITY TYPE ===================== //
    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<?> findByEntityType(@PathVariable MediaEntityType entityType) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findByEntityType(entityType), HttpStatus.OK);
    }

    // ================ FIND BY MEDIA TYPE ===================== //
    @GetMapping("/media-type/{mediaType}")
    public ResponseEntity<?> findByMediaType(@PathVariable MediaType mediaType) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findByMediaType(mediaType), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE AND ENTITY TYPE ===================== //
    @GetMapping("/institute/{instituteIdentifier}/entity-type/{entityType}")
    public ResponseEntity<?> findByInstituteIdentifierAndEntityType(@PathVariable String instituteIdentifier, @PathVariable MediaEntityType entityType) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findByInstituteIdentifierAndEntityType(instituteIdentifier, entityType), HttpStatus.OK);
    }

    // ================ FIND FEATURED MEDIA ===================== //
    @GetMapping("/featured")
    public ResponseEntity<?> findByIsFeaturedTrue() {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.findByIsFeaturedTrue(), HttpStatus.OK);
    }
}
