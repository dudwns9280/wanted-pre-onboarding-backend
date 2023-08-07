package com.example.dashboard.user.controller;

import com.example.dashboard.config.jwt.JwtToken;
import com.example.dashboard.config.jwt.TokenProvider;
import com.example.dashboard.user.dto.request.SigninRequest;
import com.example.dashboard.user.dto.request.SignupRequest;
import com.example.dashboard.user.dto.response.SigninResponse;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid SignupRequest signupRequest){
        userService.saveUser(signupRequest.getEmail(), signupRequest.getPassword());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody @Valid SigninRequest signinRequest){
        User user = userService.signin(signinRequest.getEmail(), signinRequest.getPassword());
        JwtToken jwtToken = tokenProvider.generateToken(user.getEmail());
        return ResponseEntity.ok(new SigninResponse(user, jwtToken));
    }
}
