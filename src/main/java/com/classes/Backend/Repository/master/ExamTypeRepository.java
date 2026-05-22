package com.classes.Backend.Repository.master;

import com.classes.Backend.Domain.master.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, String> {
    Optional<ExamType> findBySlug(String slug);
    List<ExamType> findByStreamIdentifier(String streamIdentifier);
}
