package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному ID")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createOrUpdateUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет существующего пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        User updatedUser = userService.createOrUpdateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-transaction-amount")
    @Operation(summary = "Получить пользователей по сумме транзакции", description = "Возвращает список пользователей, у которых есть транзакции с указанной суммой")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public ResponseEntity<List<User>> getUsersByTransactionAmount(
        @Parameter(description = "Сумма транзакции", required = true) @RequestParam Double amount) {
        List<User> users = userService.getUsersByTransactionAmount(amount);
        return ResponseEntity.ok(users);
    }
}