package com.example.dashboard.user.dto.response;

import com.example.dashboard.user.entity.User;
import com.example.dashboard.config.jwt.JwtToken;
import lombok.Getter;


@Getter
public class SigninResponse {
    private Long id;
    private String email;
    private JwtToken jwtToken;

    public SigninResponse(User user, JwtToken jwtToken){
        this.id = user.getId();
        this.email = user.getEmail();
        this.jwtToken = jwtToken;
    }

}
