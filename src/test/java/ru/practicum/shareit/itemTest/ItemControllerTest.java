package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    IItemService itemService;
    @MockBean
    private ItemMapper itemMapper;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    public static final String header = "X-Sharer-User-Id";

    @Test
    public void getByIdTest() throws Exception {
        ItemDto responseItemDto = createResponseItemDto(1L, createItemForTest());

        when(itemService.getById(any(Long.class), any(Long.class))).thenReturn(responseItemDto);

        mvc.perform(get("/items/1")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseItemDto.getId()), Long.class));

        verify(itemService, times(1)).getById(any(Long.class), any(Long.class));
    }

    @Test
    public void getAllTest() throws Exception {
        when(itemService.getAll(any(Long.class))).thenReturn(new ArrayList<>());

        mvc.perform(get("/items")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1))
                .getAll(any(Long.class));
    }

    @Test
    public void getByRequestTest() throws Exception {
        when(itemService.getByRequest(any(String.class), any(Long.class))).thenReturn(new ArrayList<>());

        mvc.perform(get("/items/search")
                        .header(header, 1L)
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1)).getByRequest(any(String.class), any(Long.class));
    }

    @Test
    public void createTest() throws Exception {
        Item item = createItemForTest();
        Item responseItem = createResponseItem(1L, item);

        when(itemService.create(any(Item.class))).thenReturn(responseItem);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseItem.getName()), String.class))
                .andExpect(jsonPath("$.description", is(responseItem.getDescription()), String.class));

        verify(itemService, times(1)).create(any(Item.class));
    }

    @Test
    public void createCommentTest() throws Exception {
        CreateCommentDto commentDto = new CreateCommentDto("comment");
        CommentDto responseCommentDto = createResponseCommentDto(1L, commentDto);

        when(itemService.createComment(any(CreateCommentDto.class), any(Long.class), any(Long.class)))
                .thenReturn(responseCommentDto);


        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(responseCommentDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.text", is(responseCommentDto.getText()), String.class));

        verify(itemService, times(1))
                .createComment(any(CreateCommentDto.class), any(Long.class), any(Long.class));
    }

    @Test
    public void updateTest() throws Exception {
        Item item = createItemForTest();
        item.setName("NewName");
        item.setDescription("Try to update name");
        ItemDto responseItemDto = createResponseItemDto(1L, item);

        when(itemService.update(any(Item.class))).thenReturn(responseItemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(item))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(responseItemDto.getDescription()), String.class));

        verify(itemService, times(1)).update(any(Item.class));
    }

    private Item createItemForTest() {
        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        return item;
    }

    private Item createResponseItem(Long id, Item item) {
        Item responseItem = new Item();
        responseItem.setId(id);
        responseItem.setName(item.getName());
        responseItem.setDescription(item.getDescription());
        responseItem.setAvailable(item.getAvailable());
        return responseItem;
    }

    private ItemDto createResponseItemDto(Long id, Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(id);
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    private CommentDto createResponseCommentDto(Long id, CreateCommentDto dto) {
        CommentDto result = new CommentDto();
        result.setId(id);
        result.setAuthorName("name");
        result.setText(dto.getText());
        result.setCreated(LocalDateTime.now());
        return result;
    }
}
