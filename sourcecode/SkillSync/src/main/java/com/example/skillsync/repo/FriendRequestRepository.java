package com.example.skillsync.repo;

import com.example.skillsync.model.FriendRequest;
import com.example.skillsync.model.FriendRequestStatus;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
}