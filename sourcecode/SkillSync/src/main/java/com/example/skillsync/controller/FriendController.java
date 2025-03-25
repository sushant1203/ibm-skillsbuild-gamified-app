package com.example.skillsync.controller;

import com.example.skillsync.model.FriendRequest;
import com.example.skillsync.service.FriendRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/friends") // Base URL for all endpoints in this controller
@CrossOrigin(origins = "http://localhost:3000") // Allows cross-origin requests from frontend running on localhost:3000
public class FriendController {
    private final FriendRequestService friendRequestService;

    // Constructor-based dependency injection for FriendRequestService
    public FriendController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    // Displays the friends page
    @GetMapping
    public String showFriendsPage() {
        return "friend";
    }

    // Sends a friend request to a user with the given username
    @PostMapping("/api/send/{receiverUsername}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable String receiverUsername) {
        return ResponseEntity.ok(friendRequestService.sendFriendRequest(receiverUsername));
    }

    // Responds to a friend request
    @PostMapping("/api/respond")
    public ResponseEntity<String> respondToRequest(
            @RequestParam String senderUsername, // Username of the sender
            @RequestParam boolean accept) { // Boolean flag to accept or reject request
        return ResponseEntity.ok(friendRequestService.respondToRequest(senderUsername, accept));
    }

    // Retrieves a list of pending friend requests
    @GetMapping("/api/pending")
    public ResponseEntity<List<Map<String, String>>> getPendingRequests() {
        List<Map<String, String>> pendingRequests = friendRequestService.getPendingRequests();
        return ResponseEntity.ok(pendingRequests);
    }

    // Retrieves a list of current friends with details
    @GetMapping("/api/list")
    public ResponseEntity<List<Map<String, String>>> getFriends() {
        try {
            List<Map<String, String>> friendsDetails = friendRequestService.getFriendsDetails();
            return ResponseEntity.ok(friendsDetails);
        } catch (Exception e) {
            // Returns an empty list with 500 Internal Server Error in case of an exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Removes a friend by their username
    @DeleteMapping("/api/remove/{friendUsername}")
    public ResponseEntity<String> removeFriend(@PathVariable String friendUsername) {
        return ResponseEntity.ok(friendRequestService.removeFriend(friendUsername));
    }
}
