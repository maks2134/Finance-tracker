package com.myfinance.financetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Files;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Log Controller", description = "API для работы с лог-файлами")
public class LogController {

    private static final String LOG_FILE_PATH = "logs/finance-tracker.log"; // Путь к лог-файлу

    @GetMapping("/download")
    @Operation(summary = "Скачать лог-файл", description = "Возвращает лог-файл в виде файла для скачивания.")
    @ApiResponse(responseCode = "200", description = "Лог-файл успешно скачан")
    @ApiResponse(responseCode = "404", description = "Лог-файл не найден")
    public ResponseEntity<Resource> downloadLogFile() throws IOException {
        Path logFilePath = Paths.get(LOG_FILE_PATH);
        File logFile = logFilePath.toFile();

        if (!logFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(logFilePath.toUri());

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    @GetMapping("/view")
    @Operation(summary = "Просмотреть лог-файл", description = "Возвращает содержимое лог-файла за указанную дату в виде текста.")
    @ApiResponse(responseCode = "200", description = "Логи успешно получены")
    @ApiResponse(responseCode = "404", description = "Лог-файл не найден")
    public ResponseEntity<String> viewLogFile(
        @Parameter(description = "Дата в формате yyyy-MM-dd", example = "2023-10-01", required = true)
        @RequestParam String date) throws IOException {
        Path logFilePath = Paths.get(LOG_FILE_PATH);
        File logFile = logFilePath.toFile();

        if (!logFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        String logContent = new String(Files.readAllBytes(logFilePath));
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(logContent);
    }
}