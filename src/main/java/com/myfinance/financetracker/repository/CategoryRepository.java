// CategoryRepository.java
package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}