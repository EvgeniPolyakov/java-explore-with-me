package ru.practicum.explorewithme.exception;

public class ErrorResponse {
    private final String status;
    private final String errors;

    public ErrorResponse(String status, String errors) {
        this.status = status;
        this.errors = errors;
    }

    public String getErrors() {
        return errors;
    }

    public String getStatus() {
        return status;
    }
}
