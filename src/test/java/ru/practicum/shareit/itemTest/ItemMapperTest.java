package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {
    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private ItemMapper mapper;

    @BeforeEach
    public void beforeEach() {
        item = new Item(1L, "name", "description", true, 1L, 2L);
        itemDto = new ItemDto(1L, "name", "description",
                true, null, null, null, 2L);
        User user = new User(1L, "name", "user@user.user");
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
    }

    @Test
    public void makeDtoTest() {
        ItemDto resultWithoutBookings = mapper.makeDto(item, List.of(comment));
        ItemDto resultWithBookings = mapper.makeDto(item, null, null, List.of(comment));

        assertNotNull(resultWithoutBookings);
        assertNotNull(resultWithBookings);
        assertEquals(item.getId(), resultWithBookings.getId());
        assertEquals(item.getId(), resultWithoutBookings.getId());
        assertEquals(item.getName(), resultWithoutBookings.getName());
        assertEquals(item.getName(), resultWithBookings.getName());
        assertEquals(item.getDescription(), resultWithoutBookings.getDescription());
        assertEquals(item.getDescription(), resultWithBookings.getDescription());
        assertEquals(item.getAvailable(), resultWithoutBookings.getAvailable());
        assertEquals(item.getAvailable(), resultWithBookings.getAvailable());
        assertFalse(resultWithoutBookings.getComments().isEmpty());
        assertFalse(resultWithBookings.getComments().isEmpty());
    }

    @Test
    public void makeModelTest() {
        Item result = ItemMapper.makeModel(itemDto, 1L);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getRequestId(), result.getRequestId());
        assertEquals(1L, result.getOwnerId());
    }

    @Test
    public void makeRequestItemDtoTest() {
        ItemInRequestDto result = ItemMapper.makeRequestItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(item.getRequestId(), result.getRequestId());
        assertEquals(item.getOwnerId(), result.getOwner());
    }

    @Test
    public void makeRequestItemDtoListTest() {
        List<ItemInRequestDto> result = ItemMapper.makeRequestItemDtoList(List.of(item));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item.getId(), result.get(0).getId());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
        assertEquals(item.getAvailable(), result.get(0).getAvailable());
        assertEquals(item.getRequestId(), result.get(0).getRequestId());
        assertEquals(item.getOwnerId(), result.get(0).getOwner());
    }
}
