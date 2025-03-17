package com.example.skillsync.service;

import com.example.skillsync.model.FriendRequest;
import com.example.skillsync.model.FriendRequestStatus;
import com.example.skillsync.model.Friendship;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.FriendRequestRepository;
import com.example.skillsync.repo.FriendshipRepository;
import com.example.skillsync.repo.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // ✅ Get the currently logged-in user
    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username);
        }
        throw new RuntimeException("User not authenticated");
    }

    // ✅ Send Friend Request (Logged-in User -> Receiver)
    public String sendFriendRequest(String receiverUsername) {
        User sender = getLoggedInUser();
        User receiver = userRepository.findByUsername(receiverUsername);

        if (receiver == null) {
            return "Receiver not found";
        }

        if (sender.equals(receiver)) {
            return "You cannot send a friend request to yourself!";
        }

        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            return "Friend request already sent!";
        }

        // ✅ Ensure the request is saved correctly
        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(request);

        return "Friend request sent!";
    }

    // ✅ Accept or Reject Friend Request (For Logged-in User)
    public String respondToRequest(String senderUsername, boolean accept) {
        User receiver = getLoggedInUser();
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

    // ✅ Get List of Friends (For Logged-in User)
    public List<Map<String, String>> getFriendsDetails() {
        User user = getLoggedInUser();

        return friendshipRepository.findByUser1OrUser2(user, user).stream()
                .map(friendship -> {
                    User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();

                    // ✅ Return username, score, and profile picture
                    Map<String, String> friendDetails = new HashMap<>();
                    friendDetails.put("username", friend.getUsername());
                    friendDetails.put("score", String.valueOf(friend.getScore()));
                    friendDetails.put("profilePicture", "/images/" + friend.getId()); // Assuming images are stored in /images/{userId}

                    return friendDetails;
                })
                .collect(Collectors.toList());
    }

    // ✅ Get Pending Friend Requests (For Logged-in User)
    public List<Map<String, String>> getPendingRequests() {
        User receiver = getLoggedInUser();

        return friendRequestRepository.findByReceiverAndStatus(receiver, FriendRequestStatus.PENDING).stream()
                .map(request -> {
                    Map<String, String> requestDetails = new HashMap<>();
                    requestDetails.put("id", request.getId().toString());
                    requestDetails.put("senderUsername", request.getSender().getUsername());
                    return requestDetails;
                })
                .collect(Collectors.toList());
    }
}
