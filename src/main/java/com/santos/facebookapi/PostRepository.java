package com.santos.facebookapi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Basic CRUD provided by JpaRepository
}
