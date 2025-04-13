package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.LogTask;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface LogService {
    String generateLogFile(String date, String logType);
    LogTask getTaskStatus(String taskId); // Изменено с Map<String, Object> на LogTask
    ResponseEntity<Resource> downloadLogFile(String taskId);
    String viewLogsByDate(String date);
}