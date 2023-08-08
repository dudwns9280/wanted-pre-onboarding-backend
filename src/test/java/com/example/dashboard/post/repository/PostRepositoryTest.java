package com.example.dashboard.post.repository;

import com.example.dashboard.post.dto.PostTestCreation;
import com.example.dashboard.post.entity.Post;
import com.example.dashboard.user.dto.UserTestCreation;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Spy
    private PostTestCreation postTestCreation;
    @Spy
    private UserTestCreation userTestCreation;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("새 게시글 작성 저장")
    public void savePost() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        User dbUser = userRepository.save(user);
        Post post = postTestCreation.createPostTest(dbUser, 1);
        // when
        Post dbPost = postRepository.save(post);

        // then
        assertEquals(dbPost.getTitle(), post.getTitle());
        assertEquals(dbPost.getContent(), post.getContent());
    }

    @Test
    @DisplayName("게시글 id로 조회")
    public void getPost() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        User dbUser = userRepository.save(user);
        Post post = postTestCreation.createPostTest(dbUser, 1);
        Post dbPost = postRepository.save(post);
        // when
        Post savedPost = postRepository.findById(dbPost.getId()).get();

        // then
        assertEquals(dbPost.getId(), savedPost.getId());
        assertEquals(dbPost.getTitle(), savedPost.getTitle());
        assertEquals(dbPost.getContent(), savedPost.getContent());
        assertEquals(dbPost, savedPost);
    }

    @Test
    @DisplayName("전체 게시글 paging 조회")
    public void getAllPostByPaging() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        User dbUser = userRepository.save(user);
        List<Post> postList = postTestCreation.createPostListTest(dbUser);
        List<Post> dbPostList = postRepository.saveAll(postList);
        Pageable pageable = PageRequest.of(1, 4);
        // when
        Page<Post> savedPostList = postRepository.findAll(pageable);

        // then
        assertEquals(dbPostList.size(), savedPostList.getContent().size());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deletePost() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        User dbUser = userRepository.save(user);
        Post post = postTestCreation.createPostTest(dbUser, 1);
        Post dbPost = postRepository.save(post);
        // when
        postRepository.delete(dbPost);

        // then
    }
}
