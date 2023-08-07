package com.example.dashboard.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;
    @Size(min=8, message = "비밀번호는 8자리 이상으로 입력해주세요")
    private String password;
}
