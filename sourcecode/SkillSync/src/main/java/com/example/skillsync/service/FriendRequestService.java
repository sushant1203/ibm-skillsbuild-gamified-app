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

        if (sender.getUsername().equals(receiver.getUsername())) {
            return "You cannot send a friend request to yourself!";
        }

        //  Prevent sending requests if a request already exists in either direction
        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent() ||
                friendRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent()) {
            return "Friend request already sent or received!";
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(request);

        return "Friend request sent!";
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

            // Ensure only one friendship entry exists
            if (!friendshipRepository.existsByUser1AndUser2(sender, receiver) &&
                    !friendshipRepository.existsByUser1AndUser2(receiver, sender)) {
                Friendship friendship = new Friendship();
                friendship.setUser1(sender);
                friendship.setUser2(receiver);
                friendshipRepository.save(friendship);
            }

            friendRequestRepository.save(request);
            return "Friend request accepted!";
        } else {
            friendRequestRepository.delete(request);
            return "Friend request rejected!";
        }
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
        User user = getLoggedInUser(); // Get the logged-in user
        User friend = userRepository.findByUsername(friendUsername);

        if (friend == null) {
            return "Friend not found";
        }

        // Delete the friendship regardless of who initiated the request
        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, friend);
        if (!friendships.isEmpty()) {
            friendshipRepository.deleteAll(friendships);
        } else {
            return "No friendship found to remove.";
        }

        // Remove any pending or past friend requests
        Optional<FriendRequest> request1 = friendRequestRepository.findBySenderAndReceiver(user, friend);
        Optional<FriendRequest> request2 = friendRequestRepository.findBySenderAndReceiver(friend, user);

        request1.ifPresent(friendRequestRepository::delete);
        request2.ifPresent(friendRequestRepository::delete);

        return "Friend removed successfully!";
    }



}
