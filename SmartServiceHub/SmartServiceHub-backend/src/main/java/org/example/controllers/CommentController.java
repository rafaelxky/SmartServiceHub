package org.example.controllers;

import org.example.models.AppUser;
import org.example.models.Comment;
import org.example.services.persistance.CommentDbService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentDbService commentDbService;

    public CommentController(CommentDbService commentDbService) {
        this.commentDbService = commentDbService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody Comment comment,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        comment.setUserId(currentUser.getId());
        Comment savedComment = commentDbService.saveComment(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/{id}")
    public Optional<Comment> getComment(@PathVariable Long id) {
        return commentDbService.getCommentById(id);
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentDbService.getAllComments();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @RequestBody Comment commentUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment existing = commentDbService.getCommentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existing.setText(commentUpdate.getText());

        return ResponseEntity.ok(commentDbService.saveComment(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment existing = commentDbService.getCommentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentDbService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}
