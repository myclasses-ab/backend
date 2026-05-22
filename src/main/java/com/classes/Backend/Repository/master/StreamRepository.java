package com.classes.Backend.Repository.master;

import com.classes.Backend.Domain.master.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreamRepository extends JpaRepository<Stream, String> {
    Optional<Stream> findBySlug(String slug);
}
