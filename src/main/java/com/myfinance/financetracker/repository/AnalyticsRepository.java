package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    Optional<Analytics> findByIdAndUser(Long id, User user);

    List<Analytics> findByUser(User user);

    void deleteByIdAndUser(Long id, User user);
}