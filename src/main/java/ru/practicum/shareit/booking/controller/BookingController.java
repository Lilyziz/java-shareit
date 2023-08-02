package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.validation.BookingDatesValidator;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;
    private final BookingMapper mapper;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDetailedDto getById(@PathVariable Long bookingId, @RequestHeader(header) Long userId) {
        log.info("Get booking with id: {}", bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDetailedDto> getAllByBooker(@RequestHeader(header) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Get list of all bookings with state: {}", state);
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDetailedDto> getAllByItemOwnerId(@RequestHeader(header) Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        log.info("Get list of all bookings with state: {}", state);
        return bookingService.getAllByItemOwnerId(userId, state);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(header) Long userId, @RequestBody BookingPostDto bookingPostDto) {
        log.info("Create booking: {}", bookingPostDto.toString());
        BookingDatesValidator.validate(bookingPostDto.getStart(), bookingPostDto.getEnd());
        return mapper.makeDto(bookingService.create(bookingPostDto, userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId, @RequestParam Boolean approved,
                             @RequestHeader(header) Long userId) {
        log.info("Update booking with id: {}", bookingId);
        return mapper.makeDto(bookingService.update(bookingId, approved, userId));
    }
}
