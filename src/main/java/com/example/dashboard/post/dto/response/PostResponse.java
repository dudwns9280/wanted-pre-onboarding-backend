package com.example.dashboard.post.dto.response;

import com.example.dashboard.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;

    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getEmail();
    }
}
