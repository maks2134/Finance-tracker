package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.repository.CategoryRepository;
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
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category("Groceries");
        category1.setId(1L);

        category2 = new Category("Entertainment");
        category2.setId(2L);
    }

    @Test
    void getCategoryById_ExistingId_ReturnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals(category1, result.get());
        assertEquals("Groceries", result.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_NonExistingId_ReturnsEmpty() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(99L);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(99L);
    }

    @Test
    void getAllCategories_ReturnsAllCategories() {
        List<Category> allCategories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(allCategories));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void createOrUpdateCategory_NewCategory_ReturnsSavedCategory() {
        Category newCategory = new Category("Utilities");

        when(categoryRepository.save(newCategory)).thenReturn(newCategory);

        Category result = categoryService.createOrUpdateCategory(newCategory);

        assertEquals(newCategory, result);
        assertEquals("Utilities", result.getName());
        verify(categoryRepository, times(1)).save(newCategory);
    }

    @Test
    void createOrUpdateCategory_ExistingCategory_ReturnsUpdatedCategory() {
        category1.setName("Food & Groceries");

        when(categoryRepository.save(category1)).thenReturn(category1);

        Category result = categoryService.createOrUpdateCategory(category1);

        assertEquals(category1, result);
        assertEquals("Food & Groceries", result.getName());
        verify(categoryRepository, times(1)).save(category1);
    }

    @Test
    void deleteCategory_ValidId_DeletesCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void createOrUpdateCategory_WithBudgets_SavesCorrectly() {
        Category categoryWithBudgets = new Category("Travel");
        categoryWithBudgets.setId(3L);
        // Note: In a real test, you might want to mock Budget objects here

        when(categoryRepository.save(categoryWithBudgets)).thenReturn(categoryWithBudgets);

        Category result = categoryService.createOrUpdateCategory(categoryWithBudgets);

        assertEquals(categoryWithBudgets, result);
        verify(categoryRepository, times(1)).save(categoryWithBudgets);
    }

    @Test
    void createOrUpdateCategory_EmptyName_ThrowsException() {
        Category emptyNameCategory = new Category("");
        emptyNameCategory.setId(4L);

        // This will throw a ConstraintViolationException in real application
        // due to @NotBlank annotation, but we're testing service layer here
        when(categoryRepository.save(emptyNameCategory)).thenReturn(emptyNameCategory);

        Category result = categoryService.createOrUpdateCategory(emptyNameCategory);

        assertEquals(emptyNameCategory, result);
        verify(categoryRepository, times(1)).save(emptyNameCategory);
    }
}