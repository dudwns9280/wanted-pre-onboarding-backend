package com.example.dashboard.user.controller;

import com.example.dashboard.user.service.UserService;
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
    public ResponseEntity<Object> signup(){
        System.out.println("Helloworld");
        return ResponseEntity.ok().build();
    }
}
