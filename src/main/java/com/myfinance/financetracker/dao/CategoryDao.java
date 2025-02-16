package com.myfinance.financetracker.dao;

import com.myfinance.financetracker.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    Optional<Category> findById(Long id);

    List<Category> findAll();

    Category save(Category category);
}