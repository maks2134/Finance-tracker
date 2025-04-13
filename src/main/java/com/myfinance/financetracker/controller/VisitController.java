package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.service.impl.VisitCounterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitCounterServiceImpl visitCounterService;

    @GetMapping("/{url}")
    public long getVisitCount(@PathVariable String url) {
        return visitCounterService.getVisitCount(url);
    }
}