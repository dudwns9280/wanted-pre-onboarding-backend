package com.example.dashboard.post.dto.response;

import com.example.dashboard.post.entity.Post;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostListResponse {
    private List<PostResponse> postResponseList;

    public PostListResponse(List<Post> postList){
        this.postResponseList = new ArrayList<>();
        for(Post post : postList){
            this.postResponseList.add(new PostResponse(post));
        }
    }
}
