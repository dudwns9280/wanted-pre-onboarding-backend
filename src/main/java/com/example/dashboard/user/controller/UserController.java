package com.example.dashboard.user.controller;

import com.example.dashboard.user.dto.SignupRequest;
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

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid SignupRequest signupRequest){
        userService.saveUser(signupRequest.getEmail(), signupRequest.getPassword());
        return ResponseEntity.ok().build();
    }
}
