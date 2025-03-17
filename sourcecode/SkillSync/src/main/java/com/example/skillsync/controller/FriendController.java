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
@RequestMapping("/friends") // This applies to all endpoints in this class
@CrossOrigin(origins = "http://localhost:3000")
public class FriendController {
    private final FriendRequestService friendRequestService;

    public FriendController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    // ✅ Serve `friend.html`
    @GetMapping
    public String showFriendsPage() {
        return "friend";
    }

    // ✅ Send Friend Request (Automatically from Logged-in User)
    @PostMapping("/api/send/{receiverUsername}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable String receiverUsername) {
        return ResponseEntity.ok(friendRequestService.sendFriendRequest(receiverUsername));
    }

    // ✅ Accept or Reject Friend Request (For Logged-in User)
    @PostMapping("/api/respond")
    public ResponseEntity<String> respondToRequest(
            @RequestParam String senderUsername,
            @RequestParam boolean accept) {
        return ResponseEntity.ok(friendRequestService.respondToRequest(senderUsername, accept));
    }

    // ✅ Get Pending Friend Requests (For Logged-in User)
    @GetMapping("/api/pending")
    public ResponseEntity<List<Map<String, String>>> getPendingRequests() {
        List<Map<String, String>> pendingRequests = friendRequestService.getPendingRequests();
        return ResponseEntity.ok(pendingRequests);
    }

    // ✅ Get Friends List (For Logged-in User)
    @GetMapping("/api/list")
    public ResponseEntity<List<Map<String, String>>> getFriends() {
        try {
            List<Map<String, String>> friendsDetails = friendRequestService.getFriendsDetails();
            return ResponseEntity.ok(friendsDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
