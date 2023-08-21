package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceTest {
    private IBookingService bookingService;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private User user;
    private Item item;
    private User owner;
    private Booking booking;
    private BookingPostDto bookingPostDto;
    private BookingMapper mapper;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        mapper = mock(BookingMapper.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository, mapper);

        bookingPostDto = new BookingPostDto(1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusWeeks(1));
        user = new User(1L, "name", "user@user.user");
        owner = new User(2L, "owner", "user2@user.user");
        item = new Item(1L, "name", "description", true, 2L, 2L);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusWeeks(1),
                item, user, BookingStatus.APPROVED);
    }

    @Test
    public void createTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.create(bookingPostDto, 1L);

        assertNotNull(result);
        assertEquals(bookingPostDto.getItemId(), result.getItem().getId());
        assertEquals(bookingPostDto.getStart().withNano(0), result.getStart().withNano(0));
        assertEquals(bookingPostDto.getEnd().withNano(0), result.getEnd().withNano(0));
    }

    @Test
    void createWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingService.create(bookingPostDto, 100L));
    }

    @Test
    void createWithWrongItemTest() {
        booking.getItem().setId(100L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingPostDto, 1L));
    }

    @Test
    public void createUnavailableBooking() {
        item.setAvailable(false);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        BadRequestException e = assertThrows(BadRequestException.class, () -> {
            bookingService.create(bookingPostDto, 1L);
        });
        assertNotNull(e);
    }

    @Test
    public void patchBookingTest() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.update(1L, true, 2L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void updateWithWrongBookingTest() {
        assertThrows(NotFoundException.class, () -> bookingService.update(100L, true, 1L));
    }

    @Test
    void updateWithWrongItemTest() {
        booking.getItem().setId(100L);
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking));

        assertThrows(NotFoundException.class, () -> bookingService.update(1L, true, 1L));
    }

    @Test
    public void patchBookingNoSuchElementExceptionTest() {
        booking.setStatus(BookingStatus.WAITING);
        item.setOwnerId(1L);
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        NotFoundException e = assertThrows(NotFoundException.class, () -> {
            bookingService.update(1L, true, 2L);
        });
        assertNotNull(e);
    }

    @Test
    public void updateBookingIllegalArgumentExceptionTest() {
        booking.setStatus(BookingStatus.WAITING);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        BadRequestException e = assertThrows(BadRequestException.class, () -> {
            bookingService.update(1L, true, 2L);
        });
        assertNotNull(e);
    }

    @Test
    public void getByIdTest() {
        item.setOwnerId(owner.getId());
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking));

        BookingDetailedDto result = bookingService.getById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getByIdWithWrongBookingTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        assertThrows(NotFoundException.class, () -> bookingService.getById(100L, 1L));
    }

    @Test
    public void getByIdNoSuchElementExceptionTest() {
        user.setId(100L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking));

        NotFoundException e = assertThrows(NotFoundException.class, () -> {
            bookingService.getById(1L, 1L);
        });
        assertNotNull(e);
    }

    @Test
    public void findAllByBookerStateRejectedTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndStatus(any(Long.class), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService.getAllByBooker(1L, "REJECTED", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateWaitingTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndStatus(any(Long.class), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService.getAllByBooker(1L, "WAITING", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateCurrentTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdCurrent(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDetailedDto> result = bookingService.getAllByBooker(1L, "CURRENT", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateFutureTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndStartIsAfter(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService.getAllByBooker(1L, "FUTURE", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStatePastTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndEndIsBefore(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService.getAllByBooker(1L, "PAST", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateAllTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDetailedDto> result = bookingService.getAllByBooker(1L, "ALL", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerUnsupportedStatus() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        UnsupportedStatusException e = assertThrows(UnsupportedStatusException.class, () -> {
            bookingService.getAllByBooker(1L, "STATUS", 0, 20);
        });
        assertNotNull(e);
    }

    @Test
    public void findAllByItemOwnerStateRejectedTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndStatus(any(Long.class), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService
                .getAllByItemOwnerId(1L, "REJECTED", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateWaitingTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndStatus(any(Long.class), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService
                .getAllByItemOwnerId(1L, "WAITING", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateCurrentTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingsByItemOwnerIdCurrent(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDetailedDto> result = bookingService
                .getAllByItemOwnerId(1L, "CURRENT", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateFutureTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndStartIsAfter(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService
                .getAllByItemOwnerId(1L, "FUTURE", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStatePastTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndEndIsBefore(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService
                .getAllByItemOwnerId(1L, "PAST", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateAllTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDetailedDto> result = bookingService
                .getAllByItemOwnerId(1L, "ALL", 0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
