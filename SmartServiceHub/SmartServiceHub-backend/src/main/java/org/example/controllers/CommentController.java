package org.example.controllers;

import org.example.models.AppUser;
import org.example.models.Comment;
import org.example.models.dto.CommentCreateDto;
import org.example.models.dto.CommentPublicDto;
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
    // todo: implement lua events here

    private final CommentDbService commentDbService;

    public CommentController(CommentDbService commentDbService) {
        this.commentDbService = commentDbService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CommentCreateDto comment,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment savedComment = commentDbService.saveComment(Comment.formCreateDto(comment, currentUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentPublicDto> getComment(@PathVariable Long id) {
        return ResponseEntity.ok(CommentPublicDto.fromComment(commentDbService.getCommentById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND))
        ));
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

        if (!currentUser.getId().equals(existing.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existing.setContent(commentUpdate.getContent());

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

        if (!currentUser.getId().equals(existing.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentDbService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{post_id}")
    public ResponseEntity<Comment> getPostComments (
            @PathVariable Long post_id
    ){
        commentDbService.getCommentsFromPost(post_id);
        return null;
    }

    @GetMapping("/unique")
    public ResponseEntity<List<Comment>> getPostUniqueComments(
        @RequestParam int limit,
        @RequestParam int offset,
        @RequestParam long post_id
    ){
        return ResponseEntity.ok(commentDbService.getCommentUnique(limit, (offset * limit), post_id));
    }
}
