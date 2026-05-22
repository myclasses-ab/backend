package com.classes.Backend.Domain.master;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "subjects")
public class Subject {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "slug", length = 100, unique = true)
    private String slug;

    @Column(name = "stream_identifier")
    private String streamIdentifier;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
