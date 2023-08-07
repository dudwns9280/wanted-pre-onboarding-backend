package com.example.dashboard.user.service;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.exception.ExceptionEnum;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void saveUser(String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST, "이미 존재하는 이메일입니다");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }
}


