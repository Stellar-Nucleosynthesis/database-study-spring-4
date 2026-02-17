package org.example.practice5.service;

import lombok.RequiredArgsConstructor;
import org.example.practice5.config.DataSourceContextHolder;
import org.example.practice5.entities.User;
import org.example.practice5.repository.UserRepository;
import org.example.practice5.util.DataSourceEnum;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final DataSourceContextHolder dataSourceContextHolder;
    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.PRIMARY);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.REPLICA);
        return userRepository.findById(id);
    }
}
