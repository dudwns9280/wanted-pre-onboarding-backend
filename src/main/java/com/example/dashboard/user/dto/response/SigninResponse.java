package com.example.dashboard.user.dto.response;

import com.example.dashboard.user.entity.User;
import com.example.dashboard.config.jwt.JwtToken;
import lombok.Data;

@Data
public class SigninResponse {
    private Long id;
    private String email;
    private JwtToken jwtToken;

    public void from(User user, JwtToken jwtToken){
        this.id = user.getId();
        this.email = user.getEmail();
        this.jwtToken = jwtToken;
    }

}
