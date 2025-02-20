package com.myfinance.financetracker.dao.impl;

import com.myfinance.financetracker.dao.CategoryDao;
import com.myfinance.financetracker.model.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private final ConcurrentHashMap<Long, Category> categoryStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CategoryDaoImpl() {
        // Предварительная инициализация тестовыми данными
        Category cat1 = new Category(idGenerator.getAndIncrement(), "Продукты");
        Category cat2 = new Category(idGenerator.getAndIncrement(), "Коммунальные услуги");
        categoryStore.put(cat1.getId(), cat1);
        categoryStore.put(cat2.getId(), cat2);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categoryStore.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categoryStore.values());
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idGenerator.getAndIncrement());
        }
        categoryStore.put(category.getId(), category);
        return category;
    }

    @Override
    public void deleteById(Long id) {
        categoryStore.remove(id);
    }
}