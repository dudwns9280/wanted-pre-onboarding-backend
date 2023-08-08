package com.example.dashboard.post.service;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.post.dto.PostTestCreation;
import com.example.dashboard.post.entity.Post;
import com.example.dashboard.post.repository.PostRepository;
import com.example.dashboard.user.dto.UserTestCreation;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Spy
    private PostTestCreation postTestCreation;
    @Spy
    private UserTestCreation userTestCreation;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;
    @Nested
    @DisplayName("게시글 작성 테스트")
    class SavePostTest{
        @Test
        @DisplayName("성공 테스트")
        void SavePostSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));
            Post post = postTestCreation.createPostTest(user, 1);

            given(postRepository.save(any())).willReturn(post);

            //when
            Post actualPost = postService.savePost(user, post.getTitle(), post.getContent());

            //then
            assertEquals(post.getTitle(), actualPost.getTitle());
            assertEquals(post.getContent(), actualPost.getContent());
            assertEquals(post, actualPost);
        }
    }

    @Nested
    @DisplayName("게시글 조회 테스트")
    class GetPostTest{
        @Test
        @DisplayName("성공 테스트")
        void GetPostSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));
            Post post = postTestCreation.createPostTest(user, 1);

            given(postRepository.findById(any())).willReturn(Optional.of(post));

            //when
            Post actualPost = postService.getPostById(post.getId());

            //then
            assertEquals(post.getTitle(), actualPost.getTitle());
            assertEquals(post.getContent(), actualPost.getContent());
            assertEquals(post, actualPost);
        }
    }

    @Nested
    @DisplayName("게시글 전체 조회 테스트")
    class GetAllPostTest{
        @Test
        @DisplayName("성공 테스트")
        void GetAllPostSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));
            List<Post> postList = postTestCreation.createPostListTest(user);
            Page<Post> postPage = postTestCreation.createPostPageTest(user);
            Pageable pageable = PageRequest.of(0, 4);

            given(postRepository.findAll(pageable)).willReturn(postPage);

            //when
            Page<Post> actualPostList = postService.findAllPostByPaging(pageable);

            //then
            assertEquals(postList, actualPostList.getContent());
            assertEquals(postList.size(), actualPostList.getContent().size());
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class UpdatePostTest{
        @Test
        @DisplayName("성공 테스트")
        void UpdatePostSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));
            Post post = postTestCreation.createPostTest(user, 1);
            Post expectedPost = postTestCreation.createPostTest(user, 2);

            given(postRepository.findById(any())).willReturn(Optional.of(post));
            given(postRepository.save(any())).willReturn(expectedPost);

            //when
            Post actualPost = postService.updatePost(user, post.getId(), expectedPost.getTitle(), expectedPost.getContent());

            //then
            assertEquals(expectedPost.getTitle(), actualPost.getTitle());
            assertEquals(expectedPost.getContent(), actualPost.getContent());
        }
        @Test
        @DisplayName("실패 테스트")
        void UpdatePostFailedTest(){
            //given
            User inputUser = userTestCreation.createUserTest(Long.valueOf(1));
            User user2 = userTestCreation.createUserTest(Long.valueOf(2));
            Post post = postTestCreation.createPostTest(user2, 1);
            Post expectedPost = postTestCreation.createPostTest(user2, 2);

            given(postRepository.findById(any())).willReturn(Optional.of(post));

            //when - then
            Throwable exception = assertThrows(
                    CommonException.class, () -> postService.updatePost(
                            inputUser,
                            post.getId(),
                            expectedPost.getTitle(),
                            expectedPost.getContent()));
            assertEquals("게시글을 수정할 권한이 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class DeletePostTest{
        @Test
        @DisplayName("성공 테스트")
        void DeletePostSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));
            Post post = postTestCreation.createPostTest(user, 1);

            given(postRepository.findById(any())).willReturn(Optional.of(post));

            //when
            postService.deletePostCheckUser(user, post.getId());

            //then
        }
        @Test
        @DisplayName("실패 테스트")
        void DeletePostFailedTest(){
            //given
            User inputUser = userTestCreation.createUserTest(Long.valueOf(1));
            User user2 = userTestCreation.createUserTest(Long.valueOf(2));
            Post post = postTestCreation.createPostTest(user2, 1);

            given(postRepository.findById(any())).willReturn(Optional.of(post));

            //when - then
            Throwable exception = assertThrows(
                    CommonException.class, () -> postService.deletePostCheckUser(
                            inputUser,
                            post.getId()));
            assertEquals("게시글을 삭제할 권한이 없습니다.", exception.getMessage());
        }
    }
}