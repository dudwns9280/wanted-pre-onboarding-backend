package com.example.dashboard.user.repository;

import com.example.dashboard.user.dto.UserTestCreation;
import com.example.dashboard.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Spy
    private UserTestCreation userTestCreation;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("새 User 저장")
    public void saveUser() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        // when
        User dbUser = userRepository.save(user);

        // then
        assertEquals(dbUser.getEmail(), user.getEmail());
        assertEquals(dbUser.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("id로 user 찾기")
    public void getUserById() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        User dbUser = userRepository.save(user);

        // when
        User savedUser = userRepository.findById(dbUser.getId()).get();

        // then
        assertEquals(dbUser.getEmail(), savedUser.getEmail());
        assertEquals(dbUser.getPassword(), savedUser.getPassword());
        assertEquals(dbUser, savedUser);
    }

    @Test
    @DisplayName("email로 user 찾기")
    public void getUserByEmail() {
        // given
        User user = userTestCreation.createUserTest(Long.valueOf(1));
        User dbUser = userRepository.save(user);

        // when
        User savedUser = userRepository.findByEmail(dbUser.getEmail()).get();

        // then
        assertEquals(dbUser.getEmail(), savedUser.getEmail());
        assertEquals(dbUser.getPassword(), savedUser.getPassword());
        assertEquals(dbUser, savedUser);
    }

}
