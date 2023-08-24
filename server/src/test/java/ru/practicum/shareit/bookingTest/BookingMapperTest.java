package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInItemDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {
    private User user;
    private Item item;
    private Booking booking;
    private BookingPostDto bookingPostDto;
    private BookingMapper mapper;

    @BeforeEach
    public void beforeEach() {
        bookingPostDto = new BookingPostDto(1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusWeeks(1));
        user = new User(1L, "name", "user@user.user");
        item = new Item(1L, "name", "description", true, 2L, 2L);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusWeeks(1),
                item, user, BookingStatus.APPROVED);
    }

    @Test
    public void makeDtoTest() {
        BookingDto result = mapper.makeDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    public void makeModelTest() {
        Booking result = mapper.makeModel(bookingPostDto, item, user);

        assertNotNull(result);
        assertEquals(bookingPostDto.getStart(), result.getStart());
        assertEquals(bookingPostDto.getEnd(), result.getEnd());
        assertEquals(item, result.getItem());
        assertEquals(user, result.getBooker());
    }

    @Test
    public void bookingInItemDtoTest() {
        BookingInItemDto result = mapper.bookingInItemDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker().getId(), result.getBookerId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    public void toDetailedDtoTest() {
        BookingDetailedDto result = mapper.toDetailedDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getBooker().getId(), result.getBooker().getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
        assertEquals(booking.getItem().getName(), result.getName());

    }

    @Test
    public void toListDetailedDtoTest() {
        List<BookingDetailedDto> result = mapper.makeDtoList(List.of(booking));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getId(), booking.getId());
    }
}
