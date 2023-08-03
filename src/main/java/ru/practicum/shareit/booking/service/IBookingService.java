package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface IBookingService {
    Booking create(BookingPostDto booking, Long userId);

    Booking update(Long bookingId, boolean approved, Long userId);

    BookingDetailedDto getById(Long bookingId, Long userId);

    List<BookingDetailedDto> getAllByBooker(Long userId, String state);

    List<BookingDetailedDto> getAllByItemOwnerId(Long userId, String stateValue);
}
