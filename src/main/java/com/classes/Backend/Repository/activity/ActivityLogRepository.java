package com.classes.Backend.Repository.activity;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.activity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, String>, JpaSpecificationExecutor<ActivityLog> {

    Page<ActivityLog> findByActorTypeAndActorIdentifier(ActivityActorType actorType, String actorIdentifier, Pageable pageable);

    @Query("""
            SELECT a FROM ActivityLog a
            WHERE a.instituteIdentifier = :instituteIdentifier
              AND a.actorType IN :actorTypes
            ORDER BY a.createdAt DESC
            """)
    Page<ActivityLog> findInstituteAdminTimeline(
            @Param("instituteIdentifier") String instituteIdentifier,
            @Param("actorTypes") List<ActivityActorType> actorTypes,
            Pageable pageable
    );

    @Query(value = """
            SELECT a FROM ActivityLog a
            WHERE (:actorType IS NULL OR a.actorType = :actorType)
              AND (:actionType IS NULL OR a.actionType = :actionType)
              AND (:entityType IS NULL OR a.entityType = :entityType)
              AND (:actorIdentifier IS NULL OR a.actorIdentifier = :actorIdentifier)
              AND (:instituteIdentifier IS NULL OR a.instituteIdentifier = :instituteIdentifier)
              AND (:fromDate IS NULL OR a.createdAt >= :fromDate)
              AND (:toDate IS NULL OR a.createdAt <= :toDate)
              AND (:searchPattern IS NULL OR LOWER(CAST(a.entityName AS text)) LIKE CAST(:searchPattern AS text)
                                OR LOWER(CAST(a.description AS text)) LIKE CAST(:searchPattern AS text)
                                OR LOWER(CAST(a.actorName AS text)) LIKE CAST(:searchPattern AS text))
            """,
            countQuery = """
            SELECT COUNT(a) FROM ActivityLog a
            WHERE (:actorType IS NULL OR a.actorType = :actorType)
              AND (:actionType IS NULL OR a.actionType = :actionType)
              AND (:entityType IS NULL OR a.entityType = :entityType)
              AND (:actorIdentifier IS NULL OR a.actorIdentifier = :actorIdentifier)
              AND (:instituteIdentifier IS NULL OR a.instituteIdentifier = :instituteIdentifier)
              AND (:fromDate IS NULL OR a.createdAt >= :fromDate)
              AND (:toDate IS NULL OR a.createdAt <= :toDate)
              AND (:searchPattern IS NULL OR LOWER(CAST(a.entityName AS text)) LIKE CAST(:searchPattern AS text)
                                OR LOWER(CAST(a.description AS text)) LIKE CAST(:searchPattern AS text)
                                OR LOWER(CAST(a.actorName AS text)) LIKE CAST(:searchPattern AS text))
            """)
    Page<ActivityLog> searchLogs(
            @Param("actorType") ActivityActorType actorType,
            @Param("actionType") ActivityActionType actionType,
            @Param("entityType") ActivityEntityType entityType,
            @Param("actorIdentifier") String actorIdentifier,
            @Param("instituteIdentifier") String instituteIdentifier,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("searchPattern") String searchPattern,
            Pageable pageable
    );

    @Query("""
            SELECT a.actorIdentifier, a.actorName, COUNT(a)
            FROM ActivityLog a
            WHERE a.actorType = :actorType AND a.createdAt >= :since
            GROUP BY a.actorIdentifier, a.actorName
            ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findTopActorsByTypeSince(
            @Param("actorType") ActivityActorType actorType,
            @Param("since") LocalDateTime since,
            Pageable pageable
    );

    @Query("""
            SELECT a.actionType, COUNT(a)
            FROM ActivityLog a
            WHERE a.createdAt >= :since
            GROUP BY a.actionType
            """)
    List<Object[]> countByActionTypeSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.createdAt >= :since")
    long countSince(@Param("since") LocalDateTime since);

    @Query("""
            SELECT a.instituteIdentifier, MAX(a.createdAt), COUNT(a)
            FROM ActivityLog a
            WHERE a.instituteIdentifier IS NOT NULL
              AND a.actorType IN :actorTypes
            GROUP BY a.instituteIdentifier
            """)
    List<Object[]> findInstituteActivitySummaries(@Param("actorTypes") List<ActivityActorType> actorTypes);

    @Query("""
            SELECT a.actorIdentifier, a.actorName, MAX(a.createdAt), COUNT(a)
            FROM ActivityLog a
            WHERE a.actorType = :actorType
            GROUP BY a.actorIdentifier, a.actorName
            """)
    List<Object[]> findActorActivitySummaries(@Param("actorType") ActivityActorType actorType);
}
