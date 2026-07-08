package com.classes.Backend.Repository.results;

import com.classes.Backend.Domain.results.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    List<Result> findByInstituteIdentifier(String instituteIdentifier);
    List<Result> findByIsFeaturedTrue();
}
