package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInItemDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingMapper {
    public static BookingDto makeDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(),
                booking.getEnd(), booking.getStatus(), booking.getItem(), booking.getBooker());
    }

    public Booking makeModel(BookingPostDto dto, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public List<BookingDetailedDto> makeDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDetailedDto).collect(Collectors.toList());
    }

    public static BookingInItemDto bookingInItemDto(Booking booking) {
        if (booking == null) return null;

        BookingInItemDto dto = new BookingInItemDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    public static BookingDetailedDto toDetailedDto(Booking booking) {
        BookingDetailedDto dto = new BookingDetailedDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBooker(booking.getBooker());
        dto.setItem(booking.getItem());
        dto.setName(booking.getItem().getName());
        return dto;
    }
}
