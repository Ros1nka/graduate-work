package ru.skypro.homework.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(int id) {
        super("Comment not found: " + id);
    }
}
