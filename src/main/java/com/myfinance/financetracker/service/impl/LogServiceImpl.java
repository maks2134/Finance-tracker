package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.LogTask;
import com.myfinance.financetracker.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogServiceImpl implements LogService {

    private static final String LOGS_DIRECTORY = "/home/Yahor/IdeaProjects/Finance-tracker/logs";
    private static final String MAIN_LOG_FILE = "/home/Yahor/IdeaProjects/Finance-tracker/logs/application.log";

    private final Map<String, LogTask> tasks = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    public String generateLogFile(String date, String logType) {
        String taskId = UUID.randomUUID().toString();
        LogTask task = new LogTask(taskId, "PENDING", date, LocalDateTime.now());
        tasks.put(taskId, task);

        processLogGenerationAsync(taskId, date, logType);
        return taskId;
    }

    @Async
    public void processLogGenerationAsync(String taskId, String date, String logType) {
        logger.info("Начало обработки задачи {}", taskId);
        LogTask task = tasks.get(taskId);

        try {
            // Этап 1: Начало обработки
            task.setStatus("PROCESSING: Reading logs");
            logger.info("Task {} - Reading logs started", taskId);
            TimeUnit.SECONDS.sleep(3);

            Path logPath = Paths.get(MAIN_LOG_FILE);
            if (!Files.exists(logPath)) {
                throw new IOException("Main log file not found");
            }

            // Этап 2: Фильтрация
            task.setStatus("PROCESSING: Filtering logs");
            logger.info("Task {} - Filtering started", taskId);
            TimeUnit.SECONDS.sleep(3);

            String filteredLogs;
            try (Stream<String> lines = Files.lines(logPath)) {
                filteredLogs = lines
                        .filter(line -> line.contains(date))
                        .filter(line -> logType == null || line.contains(logType))
                        .collect(Collectors.joining("\n"));
            }

            // Этап 3: Сохранение файла
            task.setStatus("PROCESSING: Saving file");
            logger.info("Task {} - Saving started", taskId);
            TimeUnit.SECONDS.sleep(3);

            if (filteredLogs.isEmpty()) {
                throw new IOException("No logs found");
            }

            Path outputFile = Paths.get(LOGS_DIRECTORY)
                    .resolve(String.format("logs-%s-%s.log", date, taskId));
            Files.write(outputFile, filteredLogs.getBytes());

            task.setStatus("COMPLETED");
            task.setFilePath(outputFile.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            task.setStatus("FAILED: Interrupted");
            task.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            task.setStatus("FAILED: " + e.getClass().getSimpleName());
            task.setErrorMessage(e.getMessage());
        }
    }

    @Override
    public LogTask getTaskStatus(String taskId) {
        LogTask task = tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        return task;
    }

    @Override
    public ResponseEntity<Resource> downloadLogFile(String taskId) {
        LogTask task = tasks.get(taskId);
        if (task == null || !"COMPLETED".equals(task.getStatus())) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = Paths.get(task.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + filePath.getFileName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public String viewLogsByDate(String date) {
        try {
            Path logPath = Paths.get(MAIN_LOG_FILE);
            if (!Files.exists(logPath)) {
                throw new IOException("Main log file not found");
            }

            try (Stream<String> lines = Files.lines(logPath)) {
                return lines
                        .filter(line -> line.contains(date))
                        .collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error viewing logs", e);
        }
    }
}