package org.example.controllers;

import org.example.models.AppComment;
import org.example.models.Comment;
import org.example.services.persistance.CommentDbService;
import org.springframework.web.bind.annotation.*;

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
    public Comment createComment(@RequestBody Comment comment) {
        return commentDbService.saveComment(comment);
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
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        return commentDbService.saveComment(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentDbService.deleteCommentById(id);
    }
}
