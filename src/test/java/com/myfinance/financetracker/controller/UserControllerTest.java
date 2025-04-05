package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getUserById_ShouldThrowResourceNotFoundException_WhenUserNotExists() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUserById(1L);
        });
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() {
        User user1 = new User();
        User user2 = new User();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        User user = new User();
        user.setUsername("testuser");
        when(userService.createOrUpdateUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldname");

        User updatedDetails = new User();
        updatedDetails.setUsername("newname");
        updatedDetails.setEmail("new@email.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(existingUser));
        when(userService.createOrUpdateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<User> response = userController.updateUser(1L, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newname", response.getBody().getUsername());
        assertEquals("new@email.com", response.getBody().getEmail());
    }

    @Test
    void updateUser_ShouldThrowResourceNotFoundException_WhenUserNotExists() {
        User updatedDetails = new User();
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userController.updateUser(1L, updatedDetails);
        });
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void getUsersByTransactionAmount_ShouldReturnUsersList() {
        User user1 = new User();
        User user2 = new User();
        when(userService.getUsersByTransactionAmount(100.0)).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = userController.getUsersByTransactionAmount(100.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getUsersByTransactionAmount_ShouldReturnEmptyList_WhenNoUsersFound() {
        when(userService.getUsersByTransactionAmount(100.0)).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getUsersByTransactionAmount(100.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

}