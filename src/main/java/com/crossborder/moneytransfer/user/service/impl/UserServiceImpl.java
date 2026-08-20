package com.crossborder.moneytransfer.user.service.impl;

import com.crossborder.moneytransfer.exception.ResourceNotFoundException;
import com.crossborder.moneytransfer.user.entity.User;
import com.crossborder.moneytransfer.user.repository.UserRepository;
import com.crossborder.moneytransfer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implements user lookups and writes through the JPA repository. */
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override @Transactional
    public User create(User user) { return userRepository.save(user); }
    @Override
    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }
    @Override
    public User findByEmail(String email) { return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
