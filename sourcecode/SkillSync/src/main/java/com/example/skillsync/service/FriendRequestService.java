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


    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username);
        }
        throw new RuntimeException("User not authenticated");
    }


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
                friendRequestRepository.delete(request);
            }
        }

        // Create a new request
        FriendRequest newRequest = new FriendRequest();
        newRequest.setSender(sender);
        newRequest.setReceiver(receiver);
        newRequest.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(newRequest);

        return "Friend request sent!";
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
            friendRequestRepository.save(request);


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


    public List<Map<String, String>> getFriendsDetails() {
        User user = getLoggedInUser();

        return friendshipRepository.findByUser1OrUser2(user, user).stream()
                .map(friendship -> {
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


    public List<Map<String, String>> getPendingRequests() {
        User user = getLoggedInUser();

        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING)
                .stream()
                .map(request -> {
                    Map<String, String> requestDetails = new HashMap<>();
                    requestDetails.put("id", String.valueOf(request.getId()));
                    requestDetails.put("senderUsername", request.getSender().getUsername());
                    return requestDetails;
                })
                .collect(Collectors.toList());
    }
    public String removeFriend(String friendUsername) {
        User user = getLoggedInUser(); // Get logged-in user
        User friend = userRepository.findByUsername(friendUsername);

        if (friend == null) {
            return "Friend not found";
        }


        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);


        Optional<Friendship> friendshipToDelete = friendships.stream()
                .filter(f -> (f.getUser1().equals(user) && f.getUser2().equals(friend)) ||
                        (f.getUser1().equals(friend) && f.getUser2().equals(user)))
                .findFirst();

        if (friendshipToDelete.isPresent()) {
            friendshipRepository.delete(friendshipToDelete.get());
            return "Friend removed successfully!";
        } else {
            return "Friendship not found!";
        }
    }

}
