package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления категориями.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
    Category category = categoryService.getCategoryById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    return ResponseEntity.ok(category);
  }

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody Category category) {
    Category savedCategory = categoryService.createOrUpdateCategory(category);
    return ResponseEntity.ok(savedCategory);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
    Category category = categoryService.getCategoryById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    category.setName(categoryDetails.getName());
    Category updatedCategory = categoryService.createOrUpdateCategory(category);
    return ResponseEntity.ok(updatedCategory);
  }
}