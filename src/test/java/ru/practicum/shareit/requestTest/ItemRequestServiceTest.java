package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.request.dto.PostResponseRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.IItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.service.RequestMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemRequestServiceTest {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private IItemRequestService requestService;
    private ItemRequestRepository requestRepository;
    private RequestMapper mapper;
    private User user;
    private Request request;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        requestRepository = mock(ItemRequestRepository.class);
        mapper = mock(RequestMapper.class);
        requestService = new ItemRequestServiceImpl(userRepository, itemRepository, requestRepository, mapper);

        request = new Request(1L, "description", 1L, LocalDateTime.now());
        user = new User(1L, "name", "user@user.user");
    }

    @Test
    public void createRequestTest() {
        PostRequestDto inputDto = new PostRequestDto(request.getDescription());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(requestRepository.save(any(Request.class))).thenReturn(request);

        PostResponseRequestDto responseDto = requestService.create(inputDto, 1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals(inputDto.getDescription(), responseDto.getDescription());
    }

    @Test
    void getAllByUserIdTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(requestRepository.findRequestByRequesterId(any(Long.class), any(Sort.class)))
                .thenReturn(new ArrayList<>());

        when(itemRepository.findAllByRequestId(any(Long.class))).thenReturn(new ArrayList<>());

        List<RequestWithItemsDto> result = requestService.getAllByUserId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(requestRepository.findAllByRequesterId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        when(itemRepository.findAllByRequestId(any(Long.class))).thenReturn(new ArrayList<>());

        List<RequestWithItemsDto> result = requestService.getAll(0, 20, 1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getByIdTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(requestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(request));

        when(itemRepository.findAllByRequestId(any(Long.class))).thenReturn(new ArrayList<>());

        RequestWithItemsDto result = requestService.getById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void getByIdWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> requestService.getById(1L, 100L));
    }

    @Test
    void getByIdWithWrongRequestIdTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        assertThrows(NotFoundException.class, () -> requestService.getById(100L, 1L));
    }
}
