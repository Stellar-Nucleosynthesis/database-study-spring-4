package org.example.practice5.service;

import org.example.practice5.entities.User;
import org.example.practice5.util.DataSourceEnum;

import java.util.Optional;

public interface UserService {
    User add(User user, DataSourceEnum source);
    Optional<User> findById(Integer id, DataSourceEnum source);
}
