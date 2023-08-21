package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validation.BookingDatesValidator;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;
    private final Sort sortByStartDesc = Sort.by("start").descending();

    @Override
    @Transactional
    public Booking create(BookingPostDto booking, Long userId) {
        User user = userCheck(userId);
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(
                () -> new NotFoundException("There is no item with this ID: " + booking.getItemId()));

        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("You can't book your own items");
        }
        return bookingRepository.save(mapper.makeModel(booking, item, user));
    }

    @Override
    @Transactional
    public Booking update(Long bookingId, boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("There is no booking with this ID: " + bookingId));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(
                () -> new NotFoundException("There is no item with this ID: " + booking.getItem().getId()));

        if (!item.getOwnerId().equals(userId)) {
            throw new NotFoundException("User is not the owner or a booker");
        }

        BookingStatus status = approved ? APPROVED : REJECTED;

        if (booking.getStatus().equals(status)) {
            throw new BadRequestException("State already changed");
        }
        booking.setStatus(status);

        return bookingRepository.save(booking);
    }

    @Override
    public BookingDetailedDto getById(Long bookingId, Long userId) {
        userCheck(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("There is no booking with this ID: " + bookingId));

        boolean access = userId.equals(booking.getItem().getOwnerId()) || userId.equals(booking.getBooker().getId());

        if (!access) {
            throw new NotFoundException("User is not the owner or a booker");
        }
        return mapper.toDetailedDto(booking);
    }

    @Override
    public List<BookingDetailedDto> getAllByBooker(Long userId, String stateValue, int from, int size) {
        userCheck(userId);

        State state = State.getEnumValue(stateValue);
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size, sortByStartDesc);

        switch (state) {
            case REJECTED:
                bookings = bookingRepository
                        .findByBookerIdAndStatus(userId, REJECTED, pageable).toList();
                break;
            case WAITING:
                bookings = bookingRepository
                        .findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdCurrent(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository
                        .findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable).toList();
                break;
            case ALL:
                bookings = bookingRepository
                        .findByBookerId(userId, pageable).toList();
                break;
        }
        return mapper.makeDtoList(bookings);
    }

    @Override
    public List<BookingDetailedDto> getAllByItemOwnerId(Long userId, String stateValue, int from, int size) {
        userCheck(userId);

        State state = State.getEnumValue(stateValue);
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size, sortByStartDesc);

        switch (state) {
            case REJECTED:
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndStatus(userId, REJECTED, pageable).toList();
                break;
            case WAITING:
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingsByItemOwnerIdCurrent(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable).toList();
                break;
            case ALL:
                bookings = bookingRepository
                        .findBookingByItemOwnerId(userId, pageable).toList();
                break;
        }
        return mapper.makeDtoList(bookings);
    }

    private User userCheck(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("There is no user with this ID: " + userId));
    }
}
