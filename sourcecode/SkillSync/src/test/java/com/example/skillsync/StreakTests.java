package com.example.skillsync;

import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import com.example.skillsync.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class StreakTests {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = new User();
        user.setName("TestUser");
        user.setStreak(3);

    }

    @Test
    void testStreakIncrease() {
        user.setLastLogin(LocalDate.now().minusDays(1));
        when(userRepository.findByUsername("TestUser")).thenReturn(user);
        userService.setStreak(user);
        assertEquals(4, user.getStreak());
        verify(userRepository).save(user);
    }

    @Test
    void testStreakReset() {
        user.setLastLogin(LocalDate.now().minusDays(2));
        when(userRepository.findByUsername("TestUser")).thenReturn(user);
        userService.setStreak(user);
        assertEquals(1, user.getStreak());
        verify(userRepository).save(user);
    }

}
