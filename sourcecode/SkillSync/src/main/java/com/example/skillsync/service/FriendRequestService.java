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

@Service // Marks this class as a Spring service component
public class FriendRequestService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    // Constructor-based dependency injection for repositories
    public FriendRequestService(UserRepository userRepository,
                                FriendRequestRepository friendRequestRepository,
                                FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    // Retrieves the currently logged-in user from the security context
    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username);
        }
        throw new RuntimeException("User not authenticated");
    }

    // Sends a friend request to the specified receiver username
    public String sendFriendRequest(String receiverUsername) {
        User sender = getLoggedInUser();
        User receiver = userRepository.findByUsername(receiverUsername);

        if (receiver == null) {
            return "Receiver not found";
        }

        // Check if a previous friend request exists
        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);

        if (existingRequest.isPresent()) {
            FriendRequest request = existingRequest.get();

            if (request.getStatus() == FriendRequestStatus.PENDING) {
                return "Friend request already sent!";
            } else if (request.getStatus() == FriendRequestStatus.REJECTED) {
                friendRequestRepository.delete(request); // Delete rejected request to allow resending
            }
        }

        // Create and save a new friend request
        FriendRequest newRequest = new FriendRequest();
        newRequest.setSender(sender);
        newRequest.setReceiver(receiver);
        newRequest.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(newRequest);

        return "Friend request sent!";
    }

    // Responds to a friend request by accepting or rejecting it
    public String respondToRequest(String senderUsername, boolean accept) {
        User receiver = getLoggedInUser();
        User sender = userRepository.findByUsername(senderUsername);

        if (sender == null) {
            return "Sender not found";
        }

        // Find the existing friend request
        Optional<FriendRequest> requestOpt = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (requestOpt.isEmpty()) {
            return "Friend request not found";
        }

        FriendRequest request = requestOpt.get();

        if (accept) {
            // Accept the friend request
            request.setStatus(FriendRequestStatus.ACCEPTED);
            friendRequestRepository.save(request);

            // Create a new friendship record
            Friendship friendship = new Friendship();
            friendship.setUser1(sender);
            friendship.setUser2(receiver);
            friendshipRepository.save(friendship);

            return "Friend request accepted!";
        } else {
            // Reject the friend request
            request.setStatus(FriendRequestStatus.REJECTED);
            friendRequestRepository.save(request);
            return "Friend request rejected!";
        }
    }

    // Retrieves a list of friends with their details
    public List<Map<String, String>> getFriendsDetails() {
        User user = getLoggedInUser();

        return friendshipRepository.findByUser1OrUser2(user, user).stream()
                .map(friendship -> {
                    // Determine which user is the friend
                    User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();

                    // Return only necessary details
                    Map<String, String> friendDetails = new HashMap<>();
                    friendDetails.put("username", friend.getUsername());
                    friendDetails.put("score", String.valueOf(friend.getScore()));
                    friendDetails.put("profilePicture", "/images/" + friend.getId());

                    return friendDetails;
                })
                .collect(Collectors.toList());
    }

    // Retrieves a list of pending friend requests received by the logged-in user
    public List<Map<String, String>> getPendingRequests() {
        User user = getLoggedInUser();

        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING)
                .stream()
                .map(request -> {
                    // Create a map containing request details
                    Map<String, String> requestDetails = new HashMap<>();
                    requestDetails.put("id", String.valueOf(request.getId()));
                    requestDetails.put("senderUsername", request.getSender().getUsername());
                    return requestDetails;
                })
                .collect(Collectors.toList());
    }

    // Removes a friend from the logged-in user's friend list
    public String removeFriend(String friendUsername) {
        User user = getLoggedInUser(); // Get logged-in user
        User friend = userRepository.findByUsername(friendUsername);

        if (friend == null) {
            return "Friend not found";
        }

        // Retrieve all friendships involving the logged-in user
        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);

        // Find the friendship involving the specified friend
        Optional<Friendship> friendshipToDelete = friendships.stream()
                .filter(f -> (f.getUser1().equals(user) && f.getUser2().equals(friend)) ||
                        (f.getUser1().equals(friend) && f.getUser2().equals(user)))
                .findFirst();

        if (friendshipToDelete.isPresent()) {
            friendshipRepository.delete(friendshipToDelete.get()); // Remove friendship
            return "Friend removed successfully!";
        } else {
            return "Friendship not found!";
        }
    }
}
