package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
}