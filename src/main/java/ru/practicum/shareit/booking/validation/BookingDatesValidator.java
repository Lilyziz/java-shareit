package ru.practicum.shareit.booking.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.exception.BadRequestException;

import java.time.LocalDateTime;

@UtilityClass
public class BookingDatesValidator {
    public void validate(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BadRequestException("Incorrect dates");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Incorrect dates");
        }

        if (start.equals(end)) {
            throw new BadRequestException("Incorrect dates");
        }

        if (end.isBefore(start)) {
            throw new BadRequestException("Incorrect dates");
        }
    }
}