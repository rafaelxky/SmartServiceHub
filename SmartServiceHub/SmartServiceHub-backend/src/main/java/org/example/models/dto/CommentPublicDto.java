package org.example.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.models.Comment;

import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
public class CommentPublicDto {
    public Long Id;
    public String content;
    public Long postId;
    public Long userId;
    public LocalDateTime timestamp;

    public static CommentPublicDto fromComment(Comment comment) {
        return new CommentPublicDto(comment.getId(), comment.getContent(), comment.getPostId(), comment.getUserId(), comment.getTimestamp());
    }
}
