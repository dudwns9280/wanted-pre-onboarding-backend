package com.example.dashboard.user.service;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.exception.ExceptionEnum;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new CommonException(ExceptionEnum.NOT_FOUND));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new CommonException(ExceptionEnum.NOT_FOUND));
    }
}


