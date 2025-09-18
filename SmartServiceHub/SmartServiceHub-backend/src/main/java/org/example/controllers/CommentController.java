package org.example.controllers;

import java.util.List;

import org.example.lua.LuaModManager;
import org.example.models.AppUser;
import org.example.models.Comment;
import org.example.models.dto.CommentCreateDto;
import org.example.models.dto.CommentPublicDto;
import org.example.models.responses_requests.GenericErrorResponse;
import org.example.models.responses_requests.GenericSuccessResponse;
import org.example.models.responses_requests.NotFoundResponse;
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
    public ResponseEntity<Object> createComment(
            @RequestBody CommentCreateDto comment,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in"));
        }

        Comment savedComment = commentDbService.saveComment(Comment.fromCreateDto(comment, currentUser));

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onCreateComment", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getComment(@PathVariable Long id) {

        var comment_from_db = commentDbService.getCommentById(id);
        if (comment_from_db.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Comment %d not found!".formatted(id)));
        }

        CommentPublicDto comment = CommentPublicDto.fromComment(comment_from_db.get());
        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetCommentById", null);

        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateComment(
            @PathVariable Long id,
            @RequestBody Comment commentUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in!"));
        }

        Comment existing = commentDbService.getCommentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericErrorResponse("This user cannot edit this comment because it's not its creator or doesn't have enough permissions!"));
        }

        existing.setContent(commentUpdate.getContent());

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onUpdateComment", null);



        return ResponseEntity.ok(commentDbService.saveComment(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in!"));
        }

        Comment existing = commentDbService.getCommentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericErrorResponse("This user cannot edit this comment because it's not its creator or doesn't have enough permissions!"));
        }

        commentDbService.deleteCommentById(id);

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteCommentById", null);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericSuccessResponse("Comment %d deleted successfully!"));
    }

    @GetMapping("/post/{post_id}")
    public ResponseEntity<Object> getPostComments (
            @PathVariable Long post_id
    ){
        var posts = commentDbService.getCommentsFromPost(post_id);

        if (posts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Post %d not found".formatted(post_id)));
        }

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetCommentsFromPost", null);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/unique")
    public ResponseEntity<Object> getPostUniqueComments(
        @RequestParam int limit,
        @RequestParam int offset,
        @RequestParam long post_id
    ){
        var posts = commentDbService.getCommentUnique(limit, (offset * limit), post_id);
        if (posts == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Post %d not found!".formatted(post_id)));
        }

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetUniqueComments", null);

        return ResponseEntity.ok(posts);
    }
}
