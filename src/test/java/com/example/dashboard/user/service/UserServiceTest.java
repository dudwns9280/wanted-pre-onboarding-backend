package com.example.dashboard.user.service;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.user.dto.UserTestCreation;
import com.example.dashboard.user.entity.User;
import com.example.dashboard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class UserServiceTest{

    @Spy
    private UserTestCreation userTestCreation;
    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Nested
    @DisplayName("회원 가입 테스트")
    class SignupTest{
        @Test
        @DisplayName("성공 테스트")
        void SignupSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));

            given(userRepository.save(any())).willReturn(user);
            given(passwordEncoder.encode(any())).willReturn("1234");

            //when
            userService.saveUser(user.getEmail(), user.getPassword());

            //then
        }
        @Test
        @DisplayName("이미 존재하는 email 실패 경우")
        void SignupFailedTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));

            given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
            lenient().when(userRepository.save(any())).thenReturn(user);
            lenient().when(passwordEncoder.encode(any())).thenReturn("1234");

            // when - then
            Throwable exception = assertThrows(
                    CommonException.class, () -> userService.saveUser(user.getEmail(), user.getPassword()));
            assertEquals("이미 존재하는 이메일입니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("signin 테스트")
    class SigninTest{
        @Test
        @DisplayName("성공 테스트")
        void SigninSuccessTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));

            lenient().when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            given(passwordEncoder.matches(any(), any())).willReturn(true);

            //when
            User loginUser = userService.signin(user.getEmail(), user.getPassword());

            //then
            assertEquals(user, loginUser);
            assertEquals(user.getEmail(), loginUser.getEmail());
        }
        @Test
        @DisplayName("비밀번호가 맞지 않는 경우")
        void SigninFailedTest(){
            //given
            User user = userTestCreation.createUserTest(Long.valueOf(1));

            lenient().when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            given(passwordEncoder.matches(any(), any())).willReturn(false);

            // when - then
            Throwable exception = assertThrows(
                    CommonException.class, () -> userService.signin(user.getEmail(), user.getPassword()));
            assertEquals("아이디 혹은 비밀번호 확인 요망", exception.getMessage());
        }
    }
}