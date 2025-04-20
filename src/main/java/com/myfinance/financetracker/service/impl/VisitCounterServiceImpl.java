package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Visit;
import com.myfinance.financetracker.repository.VisitRepository;
import com.myfinance.financetracker.service.VisitCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
public class VisitCounterServiceImpl implements VisitCounterService {

    private final VisitRepository visitRepository;
    private final Map<String, AtomicLong> counterCache = new ConcurrentHashMap<>();

    @Autowired
    public VisitCounterServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    public synchronized void incrementVisitCount(String url) {
        // Используем кэш для быстрого доступа
        AtomicLong counter = counterCache.computeIfAbsent(url, k -> {
            Visit visit = visitRepository.findByUrl(url);
            return new AtomicLong(visit != null ? visit.getCount() : 0);
        });

        counter.incrementAndGet();
    }

    @Override
    public long getVisitCount(String url) {
        // Проверяем кэш, затем БД
        AtomicLong cachedCount = counterCache.get(url);
        if (cachedCount != null) {
            return cachedCount.get();
        }

        Visit visit = visitRepository.findByUrl(url);
        return visit != null ? visit.getCount() : 0;
    }

    @Override
    public Map<String, Long> getAllVisits() {
        return Map.of();
    }

    @Scheduled(fixedRate = 60000) // Сохраняем в БД каждую минуту
    public void saveCountersToDatabase() {
        counterCache.forEach((url, counter) -> {
            Visit visit = visitRepository.findByUrl(url);
            if (visit == null) {
                visit = new Visit();
                visit.setUrl(url);
            }
            visit.setCount(counter.get());
            visit.setLastVisitedAt(LocalDateTime.now());
            visitRepository.save(visit);
        });
    }
}