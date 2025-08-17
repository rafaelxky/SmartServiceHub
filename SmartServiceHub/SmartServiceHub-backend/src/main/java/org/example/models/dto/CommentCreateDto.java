package org.example.models.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentCreateDto {
    public String content;
    public Long postId;
}
