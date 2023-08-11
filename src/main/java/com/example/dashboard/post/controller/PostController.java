package com.example.dashboard.post.controller;


import com.example.dashboard.config.jwt.CustomUserDetail;
import com.example.dashboard.post.dto.request.CreatePostRequest;
import com.example.dashboard.post.dto.request.UpdatePostRequest;
import com.example.dashboard.post.dto.response.PostListResponse;
import com.example.dashboard.post.dto.response.PostResponse;
import com.example.dashboard.post.entity.Post;
import com.example.dashboard.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    @PostMapping("")
    public ResponseEntity<PostResponse> createPost(@AuthenticationPrincipal CustomUserDetail customUser,
                                                         @RequestBody CreatePostRequest createPostRequest){
        Post post = postService.savePost(
                customUser.getUser(),
                createPostRequest.getTitle(),
                createPostRequest.getContent());
        return ResponseEntity.ok(new PostResponse(post));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(new PostResponse(post));
    }
    @GetMapping("")
    public ResponseEntity<PostListResponse> getAllPostByPaging(@RequestParam Integer page,
                                                               @RequestParam Integer size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Post> postList = postService.findAllPostByPaging(pageable);
        return ResponseEntity.ok(new PostListResponse(postList));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@AuthenticationPrincipal CustomUserDetail customUser,
                                                   @PathVariable Long id,
                                                   @RequestBody UpdatePostRequest updatePostRequest){
        Post post = postService.updatePost(
                customUser.getUser(),
                id,
                updatePostRequest.getTitle(),
                updatePostRequest.getContent());
        return ResponseEntity.ok(new PostResponse(post));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@AuthenticationPrincipal CustomUserDetail customUser,
                                             @PathVariable Long id){
        postService.deletePostCheckUser(customUser.getUser(), id);
        return ResponseEntity.ok().build();
    }
}
