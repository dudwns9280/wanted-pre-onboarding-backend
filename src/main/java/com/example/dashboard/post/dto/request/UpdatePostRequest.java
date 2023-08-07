package com.example.dashboard.post.dto.request;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private String title;
    private String content;
}
