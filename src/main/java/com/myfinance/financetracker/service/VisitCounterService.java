package com.myfinance.financetracker.service;


import java.util.Map;

public interface VisitCounterService {
    void incrementVisitCount(String url);
    long getVisitCount(String url);
    Map<String, Long> getAllVisits();
}