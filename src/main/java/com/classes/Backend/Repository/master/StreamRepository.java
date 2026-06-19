package com.classes.Backend.Repository.master;

import com.classes.Backend.Domain.master.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreamRepository extends JpaRepository<Stream, String> {
    Optional<Stream> findBySlug(String slug);
}
