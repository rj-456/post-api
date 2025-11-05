package com.santos.facebookapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {
        "http://localhost:5137",
        "https://post-ui-fz0e.onrender.com"
})
public class PostController {

    private final PostRepository repo;

    public PostController(PostRepository repo) {
        this.repo = repo;
    }

    // DTO for create/update requests (no jakarta.validation annotations)
    public static class PostRequest {
        public String author;
        public String content;
        public String imageUrl;
    }

    // Manual validation instead of jakarta.validation
    private List<String> validate(PostRequest req) {
        List<String> errors = new ArrayList<>();
        if (req == null) {
            errors.add("request body is required");
            return errors;
        }

        if (req.author == null || req.author.trim().isEmpty()) {
            errors.add("author is required");
        } else if (req.author.length() > 150) {
            errors.add("author must not exceed 150 characters");
        }

        if (req.content == null || req.content.trim().isEmpty()) {
            errors.add("content is required");
        }

        if (req.imageUrl != null && req.imageUrl.length() > 1000) {
            errors.add("imageUrl must not exceed 1000 characters");
        }

        return errors;
    }

    // Create a new post
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest req) {
        List<String> errors = validate(req);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        Post p = new Post(req.author.trim(), req.content.trim(), (req.imageUrl != null && req.imageUrl.trim().isEmpty()) ? null : req.imageUrl);
        Post saved = repo.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get all posts (paginating / sorting can be added later)
    @GetMapping
    public List<Post> listPosts() {
        return repo.findAll();
    }

    // Get one post by id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Optional<Post> o = repo.findById(id);
        return o.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update post (full replace semantics for author, content, imageUrl)
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest req) {
        List<String> errors = validate(req);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        return repo.findById(id)
                .map(existing -> {
                    existing.setAuthor(req.author.trim());
                    existing.setContent(req.content.trim());
                    existing.setImageUrl((req.imageUrl != null && req.imageUrl.trim().isEmpty()) ? null : req.imageUrl);
                    Post saved = repo.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}