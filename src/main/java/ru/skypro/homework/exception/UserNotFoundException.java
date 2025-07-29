package ru.skypro.homework.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String name) {
        super("User not found: " + name);
    }
}
