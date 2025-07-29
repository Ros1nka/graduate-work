package ru.skypro.homework.exception;

public class AdNotFoundException extends RuntimeException {

    public AdNotFoundException(int id) {
        super("Ad not found: " + id);
    }
}
