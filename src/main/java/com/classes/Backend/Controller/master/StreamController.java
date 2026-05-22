package com.classes.Backend.Controller.master;

import com.classes.Backend.Domain.master.Stream;
import com.classes.Backend.Service.master.StreamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/streams")
public class StreamController {

    private final StreamServiceImpl STREAM_SERVICE_IMPL;

    // ================ CREATE STREAM ===================== //
    @PostMapping
    public ResponseEntity<?> saveStream(@RequestBody Stream stream) {
        return new ResponseEntity<>(this.STREAM_SERVICE_IMPL.save(stream), HttpStatus.CREATED);
    }

    // ================ CREATE ALL STREAMS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllStreams(@RequestBody List<Stream> streams) {
        return new ResponseEntity<>(this.STREAM_SERVICE_IMPL.saveAll(streams), HttpStatus.CREATED);
    }

    // ================ GET STREAM BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getStreamById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.STREAM_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL STREAMS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllStreams() {
        List<Stream> allStreams = this.STREAM_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allStreams, HttpStatus.OK);
    }

    // ================ DELETE STREAM BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteStreamById(@PathVariable String identifier) {
        this.STREAM_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Stream deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE STREAM BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateStreamById(@PathVariable String identifier, @RequestBody Stream stream) {
        if (!this.STREAM_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Stream not found", HttpStatus.NOT_FOUND);
        }
        stream.setIdentifier(identifier);
        return new ResponseEntity<>(this.STREAM_SERVICE_IMPL.save(stream), HttpStatus.OK);
    }

    // ================ FIND BY SLUG ===================== //
    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(this.STREAM_SERVICE_IMPL.findBySlug(slug), HttpStatus.OK);
    }
}
