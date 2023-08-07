package com.example.dashboard.post.service;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.exception.ExceptionEnum;
import com.example.dashboard.post.entity.Post;
import com.example.dashboard.post.repository.PostRepository;
import com.example.dashboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(()-> new CommonException(ExceptionEnum.NOT_FOUND));
    }
    public List<Post> findAllPost(){
        return postRepository.findAll();
    }

    public Post savePost(User user, String title, String content){
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        return postRepository.save(post);
    }
    public Post updatePost(User user, Long postId, String title, String content){
        Post post = this.getPostById(postId);
        if(!user.equals(post.getUser())){
            throw new CommonException(ExceptionEnum.UNAUTHORIZED, "게시글을 수정할 권한이 없습니다.");
        }
        post.updatePost(title, content);
        return postRepository.save(post);
    }
    public void deletePostCheckUser(User user, Long postId){
        Post post = this.getPostById(postId);
        if(!user.equals(post.getUser())){
            throw new CommonException(ExceptionEnum.UNAUTHORIZED, "게시글을 삭제할 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

}


