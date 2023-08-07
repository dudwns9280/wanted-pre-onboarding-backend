package com.example.dashboard.post.service;

import com.example.dashboard.post.entity.Post;
import com.example.dashboard.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post findPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow();
    }

}


