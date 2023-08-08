package com.example.dashboard.user.controller;

import com.example.dashboard.config.jwt.CustomUserDetailService;
import com.example.dashboard.config.jwt.JwtToken;
import com.example.dashboard.config.jwt.TokenProvider;
import com.example.dashboard.config.security.SecurityConfig;
import com.example.dashboard.user.dto.UserTestCreation;
import com.example.dashboard.user.dto.request.SigninRequest;
import com.example.dashboard.user.dto.request.SignupRequest;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import({SecurityConfig.class, CustomUserDetailService.class})
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Spy
    UserTestCreation userTestCreation;
    @MockBean
    UserService userService;
    @MockBean
    CustomUserDetailService customUserDetailService;
    @MockBean
    TokenProvider tokenProvider;
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/users";

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    void signup() throws Exception{
        //given
        SignupRequest signupRequest = userTestCreation.createSignupRequestTest();
        //when
        ResultActions result = mockMvc.perform(post(baseUrl + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));
        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("signin 컨트롤러 테스트")
    void signin() throws Exception{
        //given
        SigninRequest signinRequest = userTestCreation.createSigninRequest();
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        JwtToken jwtToken = userTestCreation.createJwtTokenTest();

        given(userService.signin(any(), any())).willReturn(user);
        given(tokenProvider.generateToken(any())).willReturn(jwtToken);

        //when
        ResultActions result = mockMvc.perform(post(baseUrl + "/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest)));

        //then

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("email").value(user.getEmail()));
        result.andExpect(jsonPath("$.jwtToken.accessToken").value(jwtToken.getAccessToken()));
        result.andExpect(jsonPath("$.jwtToken.refreshToken").value(jwtToken.getRefreshToken()));

    }

}
