package com.example.dashboard.user.dto;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.exception.ExceptionEnum;
import com.example.dashboard.user.entity.User;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignupRequest {

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @Size(min=8, message = "비밀번호는 8자리 이상으로 입력해주세요")
    private String password;
}
