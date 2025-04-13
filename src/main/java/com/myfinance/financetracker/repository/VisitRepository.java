package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Visit findByUrl(String url);
}