package org.example.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class CommentPublicDto {
    public Long Id;
    public String content;
    public Long postId;
    public Long userId;
    public LocalDateTime timestamp;
}
