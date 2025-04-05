package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getCategoryById_ShouldReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getCategoryById_ShouldThrowNotFound() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryController.getCategoryById(1L);
        });
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory() {
        Category category = new Category();
        category.setName("Test Category");
        when(categoryService.createOrUpdateCategory(category)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Category", response.getBody().getName());
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() {
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        Category updatedDetails = new Category();
        updatedDetails.setName("Updated Category");

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryService.createOrUpdateCategory(existingCategory)).thenReturn(existingCategory);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Category", response.getBody().getName());
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}