package org.example.controllers;

import java.util.List;

import org.example.lua.LuaModManager;
import org.example.models.AppUser;
import org.example.models.Comment;
import org.example.models.dto.CommentCreateDto;
import org.example.models.dto.CommentPublicDto;
import org.example.services.persistance.CommentDbService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onCreateComment", null);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentPublicDto> getComment(@PathVariable Long id) {
        CommentPublicDto comment = CommentPublicDto.fromComment(commentDbService.getCommentById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND))
        );

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetCommentById", null);

        return ResponseEntity.ok(comment);
    }

    // remove probab
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

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onUpdateComment", null);



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

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteCommentById", null);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{post_id}")
    public ResponseEntity<Comment> getPostComments (
            @PathVariable Long post_id
    ){
        commentDbService.getCommentsFromPost(post_id);

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetCommentsFromPost", null);

        return null;
    }

    @GetMapping("/unique")
    public ResponseEntity<List<Comment>> getPostUniqueComments(
        @RequestParam int limit,
        @RequestParam int offset,
        @RequestParam long post_id
    ){

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetUniqueComments", null);

        return ResponseEntity.ok(commentDbService.getCommentUnique(limit, (offset * limit), post_id));
    }
}
