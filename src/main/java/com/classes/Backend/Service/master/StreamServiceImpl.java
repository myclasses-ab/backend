package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.Stream;
import com.classes.Backend.Repository.master.StreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StreamServiceImpl implements StreamService {
    private final StreamRepository STREAM_REPOSITORY;

    // ================ SAVE STREAM ===================== //
    @Override
    public Stream save(Stream stream) {
        return this.STREAM_REPOSITORY.save(stream);
    }

    // ================ SAVE ALL STREAMS ===================== //
    @Override
    public List<Stream> saveAll(List<Stream> streams) {
        return this.STREAM_REPOSITORY.saveAll(streams);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Stream> findById(String identifier) {
        return this.STREAM_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Stream> findAll() {
        return this.STREAM_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.STREAM_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Stream with identifier '" + identifier + "' not found");
        }
        this.STREAM_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.STREAM_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY SLUG ===================== //
    @Override
    public Optional<Stream> findBySlug(String slug) {
        return this.STREAM_REPOSITORY.findBySlug(slug);
    }
}
