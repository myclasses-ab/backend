package com.classes.Backend.Controller.media;

import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Service.media.MediaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    private final MediaServiceImpl MEDIA_SERVICE_IMPL;

    // ================ CREATE MEDIA ===================== //
    @PostMapping
    public ResponseEntity<?> saveMedia(@RequestBody Media media) {
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.save(media), HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteMediaById(@PathVariable String identifier) {
        this.MEDIA_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Media deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE MEDIAENTITYTYPE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateMediaById(@PathVariable String identifier, @RequestBody Media media) {
        if (!this.MEDIA_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("MediaEntityType not found", HttpStatus.NOT_FOUND);
        }
        media.setIdentifier(identifier);
        return new ResponseEntity<>(this.MEDIA_SERVICE_IMPL.save(media), HttpStatus.OK);
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
