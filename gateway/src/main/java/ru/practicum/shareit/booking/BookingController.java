package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingPostDto;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(header) Long userId, @PathVariable Long bookingId) {
        log.info("Get booking with id {} from user with id {}", bookingId, userId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(header) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(defaultValue = "20") int size) {
        log.info("Get list of all bookings with state {} from user with id {}", state, userId);
        return bookingClient.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItemOwnerId(@RequestHeader(header) Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") @Min(0) int from,
                                                      @RequestParam(defaultValue = "20") int size) {
        log.info("Get list of all bookings with state {} from user with id {}", state, userId);
        return bookingClient.getAllByItemOwnerId(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) Long userId, @RequestBody BookingPostDto bookingPostDto) {
        log.info("Create booking {} from user with id {}", bookingPostDto, userId);
        return bookingClient.create(bookingPostDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(header) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Update booking with id {} from user with ud {}", bookingId, userId);
        return bookingClient.update(bookingId, approved, userId);
    }
}
