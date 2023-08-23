package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    IBookingService bookingService;
    @MockBean
    private BookingMapper bookingMapper;
    @Autowired
    MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();
    public static final String header = "X-Sharer-User-Id";


    @Test
    public void getByIdTest() throws Exception {
        BookingDetailedDto responseDto = createDetailedDtoForTest(1L);

        when(bookingService.getById(any(Long.class), any(Long.class))).thenReturn(responseDto);

        mvc.perform(get("/bookings/1")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class));

        verify(bookingService, times(1)).getById(any(Long.class), any(Long.class));
    }

    @Test
    public void getAllByBookerTest() throws Exception {
        when(bookingService.getAllByBooker(any(Long.class), any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());

        mvc.perform(get("/bookings")
                        .header(header, 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1))
                .getAllByBooker(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));
    }

    @Test
    public void getAllByItemOwnerIdTest() throws Exception {
        when(bookingService
                .getAllByItemOwnerId(any(Long.class), any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());

        mvc.perform(get("/bookings/owner")
                        .header(header, 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1))
                .getAllByItemOwnerId(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));
    }

    @Test
    public void createTest() throws Exception {
        //волшебство с stackOverflow для того чтоб всё работало с датами
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        BookingPostDto booking = createBookingDtoForTest();
        Booking response = createBookingForTest(1L);
        when(bookingService.create(any(BookingPostDto.class), any(Long.class))).thenReturn(response);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.item", is(response.getItem()), Item.class));

        verify(bookingService, times(1)).create(any(BookingPostDto.class), any(Long.class));
    }

    @Test
    public void updateTest() throws Exception {
        Booking responseDto = createBookingForTest(1L);

        when(bookingService.update(any(Long.class), any(Boolean.class), any(Long.class)))
                .thenReturn(responseDto);

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class));

        verify(bookingService, times(1))
                .update(any(Long.class), any(Boolean.class), any(Long.class));
    }

    private BookingPostDto createBookingDtoForTest() {
        BookingPostDto booking = new BookingPostDto();
        booking.setId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.of(2023, 12, 12, 10, 0));
        booking.setEnd(LocalDateTime.of(2023, 12, 20, 10, 0));
        return booking;
    }

    private BookingDetailedDto createDetailedDtoForTest(Long id) {
        BookingDetailedDto result = new BookingDetailedDto();
        result.setId(id);
        result.setName("name");
        result.setBooker(new User());
        result.setItem(new Item());
        result.setStart(LocalDateTime.now());
        result.setEnd(LocalDateTime.now().plusWeeks(1));
        return result;
    }

    private Booking createBookingForTest(Long id) {
        Booking result = new Booking();
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("item description");
        item.setAvailable(true);

        result.setId(id);
        result.setStart(LocalDateTime.now());
        result.setEnd(LocalDateTime.now().plusWeeks(1));
        result.setItem(item);

        return result;
    }
}
