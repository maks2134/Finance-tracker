package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Visit findByUrl(String url);

    @Query("SELECT v.url, v.count FROM Visit v")
    List<Object[]> findAllUrlsAndCounts();
}