package com.example.dashboard.post.dto;

import com.example.dashboard.config.jwt.JwtToken;
import com.example.dashboard.post.dto.request.CreatePostRequest;
import com.example.dashboard.post.dto.request.UpdatePostRequest;
import com.example.dashboard.post.entity.Post;
import com.example.dashboard.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class PostTestCreation {

    public JwtToken createJwtTokenTest(){
        return JwtToken.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    public Post createPostNoneUserTest(int number){
        Post post = new Post();
        post.setId(Long.valueOf(number));
        post.setTitle("테스트 제목"+String.valueOf(number));
        post.setContent("테스트 내용"+String.valueOf(number));
        return post;
    }

    public Post createPostTest(User user, int number){
        Post post = new Post();
        post.setId(Long.valueOf(number));
        post.setTitle("테스트 제목"+String.valueOf(number));
        post.setContent("테스트 내용"+String.valueOf(number));
        post.setUser(user);
        return post;
    }
    public List<Post> createPostListTest(User user){
        List<Post> postList = new ArrayList<>();
        for(int i = 1; i<5; i++){
            postList.add(this.createPostTest(user, i));
        }
        return postList;
    }
    public Page<Post> createPostPageTest(User user){
        List<Post> postList = this.createPostListTest(user);
        return new PageImpl<>(postList);
    }

    public CreatePostRequest createPostRequestTest(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setTitle("테스트 제목1");
        createPostRequest.setContent("테스트 내용1");
        return createPostRequest;
    }
    public UpdatePostRequest updatePostRequestTest(){
        UpdatePostRequest updatePostRequest = new UpdatePostRequest();
        updatePostRequest.setTitle("테스트 제목2");
        updatePostRequest.setContent("테스트 내용2");
        return updatePostRequest;
    }

    public Post updatePostTest(Post post, UpdatePostRequest updatePostRequest){
        post.setTitle(updatePostRequestTest().getTitle());
        post.setContent(updatePostRequestTest().getContent());
        return post;
    }
}
