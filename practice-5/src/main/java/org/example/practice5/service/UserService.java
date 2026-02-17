package org.example.practice5.service;

import org.example.practice5.entities.User;

import java.util.Optional;

public interface UserService {
    User add(User user);
    Optional<User> findById(Integer id);
}
