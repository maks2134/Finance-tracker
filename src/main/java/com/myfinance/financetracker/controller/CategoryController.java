package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для управления категориями.
 */
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Controller", description = "API для управления категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить категорию по ID", description = "Возвращает категорию по указанному ID")
    @ApiResponse(responseCode = "200", description = "Категория найдена")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        return ResponseEntity.ok(category);
    }

    @GetMapping
    @Operation(summary = "Получить все категории", description = "Возвращает список всех категорий")
    @ApiResponse(responseCode = "200", description = "Список категорий успешно получен")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "Создать категорию", description = "Создает новую категорию")
    @ApiResponse(responseCode = "200", description = "Категория успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryService.createOrUpdateCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить категорию", description = "Обновляет существующую категорию по ID")
    @ApiResponse(responseCode = "200", description = "Категория успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryDetails) {
        Category category = categoryService.getCategoryById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        category.setName(categoryDetails.getName());
        Category updatedCategory = categoryService.createOrUpdateCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по ID")
    @ApiResponse(responseCode = "204", description = "Категория успешно удалена")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}