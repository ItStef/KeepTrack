package com.KeepTrack.keeptrack.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KeepTrack.keeptrack.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}