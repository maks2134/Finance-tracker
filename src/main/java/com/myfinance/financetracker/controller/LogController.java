package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.model.LogTask;
import com.myfinance.financetracker.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/generate")
    public Map<String, String> generateLogFile(
            @RequestParam String date,
            @RequestParam(required = false) String logType) {

        String taskId = logService.generateLogFile(date, logType);
        Map<String, String> response = new HashMap<>();
        response.put("taskId", taskId);
        return response;
    }

    @GetMapping("/status/{taskId}")
    public Map<String, Object> getTaskStatus(@PathVariable String taskId) {
        LogTask task = (LogTask) logService.getTaskStatus(taskId);

        Map<String, Object> response = new HashMap<>();
        response.put("taskId", task.getId());
        response.put("status", task.getStatus());
        response.put("createdAt", task.getCreatedAt());

        if (task.getErrorMessage() != null) {
            response.put("errorMessage", task.getErrorMessage());
        }

        return response;
    }

    @GetMapping("/download/{taskId}")
    public ResponseEntity<?> downloadLogFile(@PathVariable String taskId) {
        return logService.downloadLogFile(taskId);
    }

    @GetMapping("/view")
    public String viewLogs(@RequestParam String date) {
        return logService.viewLogsByDate(date);
    }
}