package com.crossborder.moneytransfer.user.repository;

import com.crossborder.moneytransfer.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Persistence gateway for user identities. */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
