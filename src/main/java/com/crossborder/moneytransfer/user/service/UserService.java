package com.crossborder.moneytransfer.user.service;

import com.crossborder.moneytransfer.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/** Defines user persistence operations required by the authentication use case. */
public interface UserService extends UserDetailsService {
    User create(User user);
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
