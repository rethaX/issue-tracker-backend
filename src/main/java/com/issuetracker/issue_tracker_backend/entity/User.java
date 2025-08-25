package com.issuetracker.issue_tracker_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String passwordHash; // Nullable for Neon Auth users

    @Column(updatable = false)
    private Instant createdAt = Instant.now();
}