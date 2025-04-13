package com.myfinance.financetracker.model;

import java.time.LocalDateTime;

public class LogTask {
    private String id;
    private String status;
    private String date;
    private String filePath;
    private String errorMessage;
    private LocalDateTime createdAt;

    public LogTask(String id, String status, String date, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.createdAt = createdAt;
    }

    // Геттеры
    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public String getFilePath() { return filePath; }
    public String getErrorMessage() { return errorMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Сеттеры
    public void setStatus(String status) { this.status = status; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}