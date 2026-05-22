package com.classes.Backend.Repository.results;

import com.classes.Backend.Domain.results.Result;
import com.classes.Backend.Domain.enums.RankOrScoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    List<Result> findByInstituteIdentifier(String instituteIdentifier);
    List<Result> findByExamTypeIdentifier(String examTypeIdentifier);
    List<Result> findByExamYear(Integer examYear);
    List<Result> findByInstituteIdentifierAndExamYear(String instituteIdentifier, Integer examYear);
    List<Result> findByIsFeaturedTrue();
    List<Result> findByIsVerifiedTrue();
    
    @Query("SELECT r FROM Result r WHERE r.rankOrScoreType = :rankOrScoreType")
    List<Result> findByRankOrScoreType(@Param("rankOrScoreType") RankOrScoreType rankOrScoreType);
}
