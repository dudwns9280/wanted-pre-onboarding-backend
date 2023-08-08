package com.example.dashboard.post.controller;

import com.example.dashboard.config.jwt.CustomUserDetail;
import com.example.dashboard.config.jwt.CustomUserDetailService;
import com.example.dashboard.config.jwt.TokenProvider;
import com.example.dashboard.config.security.SecurityConfig;
import com.example.dashboard.post.dto.PostTestCreation;
import com.example.dashboard.post.dto.request.CreatePostRequest;
import com.example.dashboard.post.dto.request.UpdatePostRequest;
import com.example.dashboard.post.entity.Post;
import com.example.dashboard.post.service.PostService;
import com.example.dashboard.user.dto.UserTestCreation;
import com.example.dashboard.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import({SecurityConfig.class, CustomUserDetailService.class})
@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {
    @Spy
    PostTestCreation postTestCreation;
    @Spy
    UserTestCreation userTestCreation;
    @MockBean
    PostService postService;
    @MockBean
    CustomUserDetailService customUserDetailService;
    @MockBean
    TokenProvider tokenProvider;
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/posts";

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        // setup customUser
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        Set<SimpleGrantedAuthority> authoritySet = userTestCreation.getAuthority();

        given(customUserDetailService.loadUserByUsername(any())).willReturn(new CustomUserDetail(user, authoritySet));
    }

    @Test
    @DisplayName("게시글 생성 컨트롤러 테스트")
    void createPostTest() throws Exception{
        //given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        CreatePostRequest createPostRequest = postTestCreation.createPostRequestTest();
        Post post = postTestCreation.createPostIncludeIdTest(user, Long.valueOf(1));
        given(postService.savePost(user, createPostRequest.getTitle(), createPostRequest.getContent())).willReturn(post);

        //when
        ResultActions result = mockMvc.perform(post(baseUrl)
                        .header(HttpHeaders.AUTHORIZATION, "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostRequest)));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("title").value(createPostRequest.getTitle()));
        result.andExpect(jsonPath("content").value(createPostRequest.getContent()));
        result.andExpect(jsonPath("writer").value(user.getEmail()));
    }

    @Test
    @DisplayName("게시글 조회 컨트롤러 테스트")
    void GetPostByIdTest() throws Exception{
        //given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        Post post = postTestCreation.createPostIncludeIdTest(user, Long.valueOf(1));
        given(postService.getPostById(Long.valueOf(1))).willReturn(post);

        //when
        ResultActions result = mockMvc.perform(get(baseUrl+"/1")
                .header(HttpHeaders.AUTHORIZATION, "accessToken")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("id").value(post.getId()));
        result.andExpect(jsonPath("title").value(post.getTitle()));
        result.andExpect(jsonPath("content").value(post.getContent()));
        result.andExpect(jsonPath("writer").value(user.getEmail()));
    }

    @Test
    @DisplayName("게시글 전체 조회 컨트롤러 테스트")
    void GetAllPostByPagingTest() throws Exception{
        //given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        Page<Post> postPage = postTestCreation.createPostPageTest(user);

        given(postService.findAllPostByPaging(any())).willReturn(postPage);

        //when
        ResultActions result = mockMvc.perform(get(baseUrl)
                .param("page","1")
                .param("size","4")
                .header(HttpHeaders.AUTHORIZATION, "accessToken")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
    }


    @Test
    @DisplayName("게시글 수정 컨트롤러 테스트")
    void UpdatePostByPagingTest() throws Exception{
        //given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        Post post = postTestCreation.createPostIncludeIdTest(user, Long.valueOf(1));
        UpdatePostRequest updatePostRequestTest = postTestCreation.updatePostRequestTest();
        Post expectedPost = postTestCreation.updatePostTest(post, updatePostRequestTest);

        given(postService.updatePost(
                user,
                post.getId(),
                updatePostRequestTest.getTitle(),
                updatePostRequestTest.getContent())).willReturn(expectedPost);

        //when
        ResultActions result = mockMvc.perform(patch(baseUrl+"/1")
                .content(objectMapper.writeValueAsString(updatePostRequestTest))
                .header(HttpHeaders.AUTHORIZATION, "accessToken")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("title").value(expectedPost.getTitle()));
        result.andExpect(jsonPath("content").value(expectedPost.getContent()));
        result.andExpect(jsonPath("writer").value(expectedPost.getUser().getEmail()));
    }


    @Test
    @DisplayName("게시글 삭제 컨트롤러 테스트")
    void DeletePostByPagingTest() throws Exception{
        //given

        //when
        ResultActions result = mockMvc.perform(delete(baseUrl+"/1")
                .header(HttpHeaders.AUTHORIZATION, "accessToken")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
    }
}
