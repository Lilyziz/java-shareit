package ru.practicum.shareit.requestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.request.dto.PostResponseRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.service.IItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    IItemRequestService itemRequestService;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mvc;
    public static final String header = "X-Sharer-User-Id";

    @Test
    public void createTest() throws Exception {
        PostRequestDto requestDto = createPostRequestDto("description");
        PostResponseRequestDto responseDto = createPostResponseDto(1L, requestDto, LocalDateTime.now());

        when(itemRequestService.create(any(PostRequestDto.class), any(Long.class))).thenReturn(responseDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(responseDto.getDescription()), String.class));

        verify(itemRequestService, times(1)).create(any(PostRequestDto.class), any(Long.class));
    }

    @Test
    public void getAllByUserIdTest() throws Exception {
        when(itemRequestService.getAllByUserId(any(Long.class))).thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).getAllByUserId(any(Long.class));
    }

    @Test
    public void getAllTest() throws Exception {
        when(itemRequestService.getAll(any(Integer.class), any(Integer.class), any(Long.class)))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "20")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1))
                .getAll(any(Integer.class), any(Integer.class), any(Long.class));
    }

    @Test
    public void getByIdTest() throws Exception {
        RequestWithItemsDto dto = new RequestWithItemsDto();
        dto.setId(1L);
        dto.setDescription("description");
        dto.setItems(Collections.emptyList());

        when(itemRequestService.getById(any(Long.class), any(Long.class))).thenReturn(dto);

        mvc.perform(get("/requests/1")
                        .header(header, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription()), String.class))
                .andExpect(jsonPath("$.items", is(dto.getItems()), List.class));

        verify(itemRequestService, times(1)).getById(any(Long.class), any(Long.class));
    }

    private PostResponseRequestDto createPostResponseDto(Long id, PostRequestDto dto, LocalDateTime date) {
        PostResponseRequestDto responseDto = new PostResponseRequestDto();
        responseDto.setDescription(dto.getDescription());
        responseDto.setId(1L);
        responseDto.setId(id);
        responseDto.setCreated(date);
        return responseDto;
    }

    private PostRequestDto createPostRequestDto(String description) {
        return new PostRequestDto(description);
    }
}
