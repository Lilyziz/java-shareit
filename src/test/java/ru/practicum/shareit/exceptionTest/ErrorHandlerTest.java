package ru.practicum.shareit.exceptionTest;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorHandlerTest {
    private final ErrorHandler handler = new ErrorHandler();
    @Test
    public void handleUnsupportedStatusExceptionTest() {
        UnsupportedStatusException e = new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    public void internalServerErrorExceptionTest() {
        InternalServerErrorException e = new InternalServerErrorException("Error");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
    }
}
