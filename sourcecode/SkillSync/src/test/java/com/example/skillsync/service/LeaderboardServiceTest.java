package com.example.skillsync.service;

import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaderboardServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTopUsers_ShouldReturnTopUsers() {
        // Arrange
        int limit = 3;
        List<User> expectedUsers = Arrays.asList(
            createUser("user1", 100),
            createUser("user2", 90),
            createUser("user3", 80)
        );
        when(userService.getTopUsersByScore(limit)).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = leaderboardService.getTopUsers(limit);

        // Assert
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getUsername(), actualUsers.get(0).getUsername());
        assertEquals(expectedUsers.get(1).getUsername(), actualUsers.get(1).getUsername());
        assertEquals(expectedUsers.get(2).getUsername(), actualUsers.get(2).getUsername());
        verify(userService).getTopUsersByScore(limit);
    }

    @Test
    void getTopFriendsUsers_ShouldReturnTopFriends() {
        // Arrange
        int limit = 2;
        List<User> expectedFriends = Arrays.asList(
            createUser("friend1", 150),
            createUser("friend2", 120)
        );
        when(userService.getTopFriendsUsersByScore(limit)).thenReturn(expectedFriends);

        // Act
        List<User> actualFriends = leaderboardService.getTopFriendsUsers(limit);

        // Assert
        assertEquals(expectedFriends.size(), actualFriends.size());
        assertEquals(expectedFriends.get(0).getUsername(), actualFriends.get(0).getUsername());
        assertEquals(expectedFriends.get(1).getUsername(), actualFriends.get(1).getUsername());
        verify(userService).getTopFriendsUsersByScore(limit);
    }

    @Test
    void getTopUsers_WithZeroLimit_ShouldReturnEmptyList() {
        // Arrange
        int limit = 0;
        when(userService.getTopUsersByScore(limit)).thenReturn(Arrays.asList());

        // Act
        List<User> actualUsers = leaderboardService.getTopUsers(limit);

        // Assert
        assertTrue(actualUsers.isEmpty());
        verify(userService).getTopUsersByScore(limit);
    }

    private User createUser(String username, int score) {
        User user = new User();
        user.setUsername(username);
        user.setScore(score);
        return user;
    }
} 