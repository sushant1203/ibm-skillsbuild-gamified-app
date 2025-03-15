package com.example.skillsync.service;

import com.example.skillsync.model.FriendRequest;
import com.example.skillsync.model.FriendRequestStatus;
import com.example.skillsync.model.Friendship;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.FriendRequestRepository;
import com.example.skillsync.repo.FriendshipRepository;
import com.example.skillsync.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    public FriendRequestService(UserRepository userRepository,
                                FriendRequestRepository friendRequestRepository,
                                FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    // ✅ Send Friend Request
    public String sendFriendRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername);
        if (sender == null) {
            return "Sender not found";
        }

        User receiver = userRepository.findByUsername(receiverUsername);
        if (receiver == null) {
            return "Receiver not found";
        }

        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            return "Friend request already sent!";
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(request);

        return "Friend request sent!";
    }

    // ✅ Accept or Reject Friend Request
    public String respondToRequest(String receiverUsername, String senderUsername, boolean accept) {
        User receiver = userRepository.findByUsername(receiverUsername);
        if (receiver == null) {
            return "Receiver not found";
        }

        User sender = userRepository.findByUsername(senderUsername);
        if (sender == null) {
            return "Sender not found";
        }

        Optional<FriendRequest> requestOpt = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (requestOpt.isEmpty()) {
            return "Friend request not found";
        }

        FriendRequest request = requestOpt.get();

        if (accept) {
            request.setStatus(FriendRequestStatus.ACCEPTED);
            friendRequestRepository.save(request);

            // ✅ Create Friendship
            Friendship friendship = new Friendship();
            friendship.setUser1(sender);
            friendship.setUser2(receiver);
            friendshipRepository.save(friendship);

            return "Friend request accepted!";
        } else {
            request.setStatus(FriendRequestStatus.REJECTED);
            friendRequestRepository.save(request);
            return "Friend request rejected!";
        }
    }

    // ✅ Get Friends' Usernames Only (Fixes API Overload Issue)
    public List<String> getFriendsUsernames(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return friendshipRepository.findByUser1OrUser2(user, user).stream()
                .map(friendship -> friendship.getUser1().equals(user) ? friendship.getUser2().getUsername() : friendship.getUser1().getUsername())
                .distinct() // ✅ Prevent duplicates
                .collect(Collectors.toList());
    }

    // ✅ Get Pending Friend Requests
    public List<FriendRequest> getPendingRequests(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
    }
}
