package ru.job4j.todo.exception;

public class EmailReservedException extends ServiceException {

    public EmailReservedException(String message) {
        super(message);
    }

    public EmailReservedException(String message, Throwable cause) {
        super(message, cause);
    }
}