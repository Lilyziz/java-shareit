package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface IItemService {
    Item create(Item item);

    ItemDto update(Item item);

    ItemDto getById(Long itemId, Long userId);

    List<ItemDto> getAll(Long userId);

    List<ItemDto> getByRequest(String request, Long userId);

    CommentDto createComment(CreateCommentDto dto, Long itemId, Long userId);
}
