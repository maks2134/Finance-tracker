package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Visit;
import com.myfinance.financetracker.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class VisitCounterServiceImpl {

    @Autowired
    private VisitRepository visitRepository;

    @Transactional
    public void incrementVisitCount(String url) {
        Visit visit = visitRepository.findByUrl(url);

        if (visit == null) {
            visit = new Visit();
            visit.setUrl(url);
            visit.setCount(1L);
        } else {
            visit.setCount(visit.getCount() + 1);
        }

        visit.setLastVisitedAt(LocalDateTime.now());
        visitRepository.save(visit);
    }

    /**
     * Возвращает количество посещений для URL
     */
    public long getVisitCount(String url) {
        Visit visit = visitRepository.findByUrl(url);
        return visit == null ? 0 : visit.getCount();
    }
}