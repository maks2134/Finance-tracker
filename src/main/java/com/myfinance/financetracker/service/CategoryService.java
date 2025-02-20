package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> getCategoryById(Long id);

    List<Category> getAllCategories();

    Category createOrUpdateCategory(Category category);

    void deleteCategory(Long id);
}