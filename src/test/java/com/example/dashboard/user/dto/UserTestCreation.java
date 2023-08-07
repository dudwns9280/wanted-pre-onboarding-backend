package com.example.dashboard.user.dto;

import com.example.dashboard.config.jwt.JwtToken;
import com.example.dashboard.user.dto.request.SigninRequest;
import com.example.dashboard.user.dto.request.SignupRequest;
import com.example.dashboard.user.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class UserTestCreation {

    private final String email="dudwns9280@gmail.com";
    private final String password="12345678";

    public SignupRequest createSignupRequestTest(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        return signupRequest;
    }
    public SigninRequest createSigninRequest(){
        SigninRequest loginRequest = new SigninRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        return loginRequest;
    }

    public JwtToken createJwtTokenTest(){
        return JwtToken.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    public User createUserTest(){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
