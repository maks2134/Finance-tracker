package com.myfinance.financetracker.service;


public interface VisitCounterService
{
    void incrementVisitCount(String url);

    long getVisitCount(String url);
}