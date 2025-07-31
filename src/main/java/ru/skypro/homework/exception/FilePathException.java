package ru.skypro.homework.exception;

import java.io.IOException;

public class FilePathException extends IOException {
    public FilePathException(String message) {
        super(message);
    }
}
