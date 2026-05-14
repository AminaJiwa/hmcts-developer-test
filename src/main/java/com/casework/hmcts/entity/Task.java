package com.casework.hmcts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, length = 100)
    private String status;

    // Maps Java field name to database column since SQL generally uses snake case
    @Column(name = "due_date_time", nullable = false)
    private LocalDateTime dueDateTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Task() {
        // Default constructor for JPA
    }

    public Task(String title, String description, String status, LocalDateTime dueDateTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDateTime = dueDateTime;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public LocalDateTime getDueDateTime() { return dueDateTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setDueDateTime(LocalDateTime dueDateTime) { this.dueDateTime = dueDateTime; }

    // Automatically called by Hibernate before saving to database
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
