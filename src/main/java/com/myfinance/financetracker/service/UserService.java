package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User createOrUpdateUser(User user);

    void deleteUser(Long id);
}