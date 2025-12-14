package com.iremayvaz.service;

import com.iremayvaz.model.entity.User;
import com.iremayvaz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public Long count() {
        return userRepository.count();
    }

    public User findByEmailOrNull(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }
}
