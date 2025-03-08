package com.example.task_manager.service;

import com.example.task_manager.model.User;
import com.example.task_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Попытка входа с email: {}", username);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.warn("Пользователь с email: {} не найден", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }

    public void saveUser(User user, boolean isAdmin) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (isAdmin) {
            user.setRoles(Arrays.asList("USER", "ADMIN"));
        } else {
            user.setRoles(Arrays.asList("USER"));
        }
        userRepository.save(user);
    }
}
