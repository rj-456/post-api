package com.santos.facebookapi;

// Make sure to use jakarta.persistence, NOT javax.persistence
import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant; // Using Instant is modern practice

@Entity
@Table(name = "posts") // This ensures the table name is 'posts'
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(nullable = false)
    private String content;

    @Column(length = 1000)
    private String imageUrl;

    // --- THIS IS THE FIX ---
    @CreationTimestamp // This automatically sets the time when a new post is created
    @Column(nullable = false, updatable = false) // Ensures it's not null and cannot be changed
    private Instant createdDate;

    // --- THIS IS A GOOD ADDITION ---
    @UpdateTimestamp // This automatically updates the time when a post is edited
    @Column(nullable = false)
    private Instant modifiedDate;

    // --- IMPORTANT: Add a no-argument constructor for JPA ---
    public Post() {
    }

    // This is the constructor your controller is using
    public Post(String author, String content, String imageUrl) {
        this.author = author;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    // --- GETTERS AND SETTERS ---
    // (You can auto-generate these in IntelliJ)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}