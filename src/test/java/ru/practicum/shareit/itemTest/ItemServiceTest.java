package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    private IItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private Item item;
    private User user;
    private User secondUser;
    private ItemDto itemDto;
    private Comment comment;
    private Booking booking;
    private CreateCommentDto createCommentDto;
    @MockBean
    private ItemMapper itemMapper;

    @BeforeEach
    public void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemMapper = mock(ItemMapper.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository,
                commentRepository, itemMapper);

        item = new Item(1L, "name", "description", true, 1L, 3L);
        itemDto = new ItemDto(1L, "name", "description",
                true, null, null, null, 2L);
        user = new User(1L, "name", "user@user.user");
        secondUser = new User(3L, "name", "user@user.user");
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
        createCommentDto = new CreateCommentDto("comment");
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                item, user, BookingStatus.APPROVED);
    }

    @Test
    public void createTest() {
        when(userRepository.findAll()).thenReturn(List.of(secondUser));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.create(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(item.getRequestId(), result.getRequestId());
    }

    @Test
    public void createOwnerNotFoundTest() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        NotFoundException e = assertThrows(NotFoundException.class, () -> {
            itemService.create(item);
        });
        assertNotNull(e);
    }

    @Test
    public void updateTest() {
        itemDto.setName("updatedName");
        item.setName("updatedName");

        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        ItemDto result = itemService.update(item);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
    }

    @Test
    public void updateByNotOwnerTest() {
        item.setOwnerId(100L);
        assertThrows(NotFoundException.class, () -> itemService.update(item));
    }

    @Test
    public void createCommentForWrongItemTest() {
        assertThrows(NotFoundException.class, () -> itemService.createComment(createCommentDto, 100L, 1L));
    }

    @Test
    public void createCommentForWrongUserTest() {
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        assertThrows(NotFoundException.class, () -> itemService.createComment(createCommentDto, 1L, 100L));
    }

    @Test
    public void createBlankCommentTest() {
        CreateCommentDto dto = new CreateCommentDto(" ");
        assertThrows(BadRequestException.class, () -> itemService.createComment(dto, 1L, 1L));
    }


    @Test
    public void getByIdTest() {
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        ItemDto result = itemService.getById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    public void getByIdWithWrongItemIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.getById(100L, 1L));
    }

    @Test
    public void test() {
        item.setRequestId(3L);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertNotNull(itemService.getById(1L, 3L));
    }

    @Test
    public void createCommentTest() {
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingsForAddComments(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.createComment(createCommentDto, 1L, 1L);

        assertNotNull(result);
        assertEquals(createCommentDto.getText(), result.getText());
        assertEquals(user.getName(), result.getAuthorName());
    }

    @Test
    public void getAllTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(any(Long.class))).thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.getAll(1L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllForWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemService.getAll(100L));
    }

    @Test
    public void findItemsByRequestTest() {
        when(itemRepository.search(any(String.class))).thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.getByRequest("request", 1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByEmptyRequest() {
        List<ItemDto> result = itemService.getByRequest("", 1L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getItemsBySeveralRequestsTest() {
        Item item2 = new Item(2L, "item2", "description", true, 3L, 3L);
        List<Item> items = List.of(item, item2);
        when(itemRepository.search(any(String.class))).thenReturn(new ArrayList<>(items));
        item.setOwnerId(3L);
        List<ItemDto> result = itemService.getByRequest("description", 3L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
