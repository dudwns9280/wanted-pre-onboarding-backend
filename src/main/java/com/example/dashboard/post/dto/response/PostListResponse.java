package com.example.dashboard.post.dto.response;

import com.example.dashboard.post.entity.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostListResponse {
    private List<PostResponse> postResponseList;
    private int totalPage;

    public PostListResponse(Page<Post> postList){
        this.postResponseList = new ArrayList<>();
        for(Post post : postList.getContent()){
            this.postResponseList.add(new PostResponse(post));
        }
        this.totalPage = postList.getTotalPages();
    }
}
