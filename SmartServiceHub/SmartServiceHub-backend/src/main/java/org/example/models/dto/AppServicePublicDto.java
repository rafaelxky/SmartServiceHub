package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AppServicePublicDto {
    public Long id;
    public String title;
    public String content;
    public Long userId;
    public LocalDateTime timestamp;
}
