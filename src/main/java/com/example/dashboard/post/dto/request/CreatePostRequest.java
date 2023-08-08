package com.example.dashboard.post.dto.request;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String title;
    private String content;
}
