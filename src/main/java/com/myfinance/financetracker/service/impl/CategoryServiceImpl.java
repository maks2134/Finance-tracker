package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.dao.CategoryDao;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryDao categoryDao;

  @Autowired
  public CategoryServiceImpl(CategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }

  @Override
  public Optional<Category> getCategoryById(Long id) {
    return categoryDao.findById(id);
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryDao.findAll();
  }

  @Override
  public Category createOrUpdateCategory(Category category) {
    return categoryDao.save(category);
  }
}