package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {
    private Item item;
    private User user;
    private Comment comment;
    private CreateCommentDto createCommentDto;
    private CommentMapper mapper;

    @BeforeEach
    public void beforeEach() {
        item = new Item(1L, "name", "description", true, 1L, 2L);
        user = new User(1L, "name", "user@user.user");
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
        createCommentDto = new CreateCommentDto("comment");
    }

    @Test
    public void toModelTest() {
        Comment result = mapper.toModel(createCommentDto, item, user);

        assertNotNull(result);
        assertEquals(createCommentDto.getText(), result.getText());
        assertEquals(user.getId(), result.getAuthor().getId());
        assertEquals(item.getId(), result.getItem().getId());
    }

    @Test
    public void toCommentDetailedDtoTest() {
        CommentDto result = mapper.toCommentDetailedDto(comment);

        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getAuthor().getName(), result.getAuthorName());
        assertEquals(comment.getText(), result.getText());
    }

    @Test
    public void toCommentDetailedDtoListTest() {
        List<CommentDto> result = mapper.toCommentDetailedDtoList(List.of(comment));

        assertNotNull(result);
        assertEquals(result.get(0).getId(), comment.getId());
        assertEquals(result.get(0).getText(), comment.getText());
        assertEquals(result.get(0).getAuthorName(), comment.getAuthor().getName());
    }
}
