package com.example.skillsync;

import com.example.skillsync.controller.AuthController;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import com.example.skillsync.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class StreakTests {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private AuthController authController;

    private User user;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        user = new User(); // Setting up user for test
        user.setName("TestUser");
        user.setStreak(3);

        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test // Testing the streak increase by setting the user's last login to the day before, and updating streak
    void testStreakIncrease() {
        user.setLastLogin(LocalDate.now().minusDays(1)); // Sets to yesterday
        when(userRepository.findByUsername("TestUser")).thenReturn(user);
        userService.setStreak(user); // Setting streak
        assertEquals(4, user.getStreak()); // Checking result
        verify(userRepository).save(user);
    }

    @Test // Testing the streak by setting the user's last login to 2 days ago, so update should reset streak
    void testStreakReset() {
        user.setLastLogin(LocalDate.now().minusDays(2)); // Sets to 2 days ago
        when(userRepository.findByUsername("TestUser")).thenReturn(user);
        userService.setStreak(user); // Setting streak
        assertEquals(1, user.getStreak()); // Checking result
        verify(userRepository).save(user);
    }

    @Test // Not in use
    void testStreakRegister() throws Exception {
        mockMvc.perform(post("/register")
                .param("name","TestUser1")
                .param("password","TestUser1!")
                .param("username","TestUser1")
                .param("email","TestUser@Email.com")
        );
        when(userRepository.findByUsername("TestUser1")).thenReturn(user);
        userRepository.save(user);
        assertEquals(1, user.getStreak());
    }

}
