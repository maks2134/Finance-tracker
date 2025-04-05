package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        // Initialize users with all required fields
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("John");
        user1.setEmail("john.doe@example.com");
        // Set any other required fields

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Jane");
        user2.setEmail("jane.smith@example.com");
        // Set any other required fields

        userList = Arrays.asList(user1, user2);
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_NonExistingId_ReturnsEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(99L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void getAllUsers_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(userList));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createOrUpdateUser_CreatesNewUser_ReturnsSavedUser() {
        User newUser = new User();
        newUser.setUsername("New");
        newUser.setEmail("new.user@example.com");
        // Set any other required fields

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setUsername("New");
        savedUser.setEmail("new.user@example.com");

        when(userRepository.save(newUser)).thenReturn(savedUser);

        User result = userService.createOrUpdateUser(newUser);

        assertEquals(savedUser, result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void createOrUpdateUser_UpdatesExistingUser_ReturnsUpdatedUser() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("John");
        updatedUser.setEmail("john.doe.updated@example.com");
        // Set any other required fields

        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.createOrUpdateUser(updatedUser);

        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void deleteUser_ValidId_DeletesUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUsersByTransactionAmount_ReturnsFilteredUsers() {
        Double amount = 100.0;
        when(userRepository.findUsersByTransactionAmount(amount)).thenReturn(userList);

        List<User> result = userService.getUsersByTransactionAmount(amount);

        assertEquals(userList, result);
        verify(userRepository, times(1)).findUsersByTransactionAmount(amount);
    }

    @Test
    void getUsersByTransactionAmount_NoUsers_ReturnsEmptyList() {
        Double amount = 1000.0;
        when(userRepository.findUsersByTransactionAmount(amount)).thenReturn(List.of());

        List<User> result = userService.getUsersByTransactionAmount(amount);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findUsersByTransactionAmount(amount);
    }
}