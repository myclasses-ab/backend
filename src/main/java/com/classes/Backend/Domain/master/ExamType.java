package com.classes.Backend.Domain.master;

import com.classes.Backend.Domain.enums.ExamLevel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "exam_types")
public class ExamType {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "slug", length = 100, unique = true)
    private String slug;

    @Column(name = "stream_identifier")
    private String streamIdentifier;

    @Column(name = "standard", length = 10)
    private String standard;

    @Column(name = "conducting_body", length = 200)
    private String conductingBody;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_level")
    private ExamLevel examLevel;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "display_order")
    private Integer displayOrder;
}
