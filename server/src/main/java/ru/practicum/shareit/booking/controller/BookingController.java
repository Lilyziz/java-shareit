package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.booking.validation.BookingDatesValidator;

import java.util.List;

@Validated
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final BookingMapper mapper;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDetailedDto getById(@RequestHeader(header) Long userId, @PathVariable Long bookingId) {
        log.info("Get booking with id {} from user with id {}", bookingId, userId);
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDetailedDto> getAllByBooker(@RequestHeader(header) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "20") int size) {
        log.info("Get list of all bookings with state {} from user with id {}", state, userId);
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDetailedDto> getAllByItemOwnerId(@RequestHeader(header) Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        log.info("Get list of all bookings with state {} from user with id {}", state, userId);
        return bookingService.getAllByItemOwnerId(userId, state, from, size);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(header) Long userId, @RequestBody BookingPostDto bookingPostDto) {
        log.info("Create booking {} from user with id {}", bookingPostDto, userId);
        BookingDatesValidator.validate(bookingPostDto.getStart(), bookingPostDto.getEnd());
        return mapper.makeDto(bookingService.create(bookingPostDto, userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(header) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Update booking with id {} from user with ud {}", bookingId, userId);
        return mapper.makeDto(bookingService.update(bookingId, approved, userId));
    }
}
