package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.skypro.homework.exception.ImageUploadException;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice // Делает этот класс глобальным обработчиком исключений для всех @RestController
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<Map<String, String>> handleImageUploadException(ImageUploadException ex) {
        logger.warn("Image upload failed: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("error", "Image upload failed: " + ex.getMessage()));
    }
}