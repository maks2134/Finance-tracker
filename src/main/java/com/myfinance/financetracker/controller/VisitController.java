package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.service.VisitCounterService;
import com.myfinance.financetracker.service.impl.VisitCounterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitCounterService visitCounterService;

    @Autowired
    public VisitController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    // Получение счетчика по конкретному URL
    @GetMapping("/count")
    public long getVisitCount(@RequestParam String url) {
        return visitCounterService.getVisitCount(url);
    }

    // Получение всех счетчиков
    @GetMapping
    public Map<String, Long> getAllVisits() {
        return visitCounterService.getAllVisits();
    }
}