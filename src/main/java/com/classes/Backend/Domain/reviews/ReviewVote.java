package com.classes.Backend.Domain.reviews;

import com.classes.Backend.Domain.enums.VoteType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "review_votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"review_identifier", "user_identifier"})
})
public class ReviewVote {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "review_identifier")
    private String reviewIdentifier;

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote")
    private VoteType vote;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
