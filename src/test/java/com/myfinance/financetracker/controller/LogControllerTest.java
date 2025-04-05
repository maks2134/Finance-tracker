package com.myfinance.financetracker.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

    @InjectMocks
    private LogController logController;

    @Test
    void downloadLogFile_ShouldReturnBadRequestForInvalidDate() throws IOException
    {
        ResponseEntity<Resource> response = logController.downloadLogFile("invalid-date");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void downloadLogFile_ShouldReturnNotFoundForMissingFile() throws IOException {
        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false);

            ResponseEntity<Resource> response = logController.downloadLogFile("2023-01-01");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void viewLogFile_ShouldReturnFilteredLogs() throws IOException {
        try (var mockedFiles = mockStatic(Files.class)) {
            // Mock file existence
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);

            // Mock file content
            String testLogs = "2023-01-01 INFO Test log\n2023-01-02 INFO Another log";
            mockedFiles.when(() -> Files.lines(any(Path.class)))
                .thenReturn(java.util.stream.Stream.of(testLogs.split("\n")));

            ResponseEntity<String> response = logController.viewLogFile("2023-01-01");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("2023-01-01"));
            assertFalse(response.getBody().contains("2023-01-02"));
        }
    }

    @Test
    void viewLogFile_ShouldReturnNotFoundForNoMatchingLogs() throws IOException {
        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.lines(any(Path.class)))
                .thenReturn(java.util.stream.Stream.empty());

            ResponseEntity<String> response = logController.viewLogFile("2023-01-01");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}