package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final IItemService itemService;
    private final ItemMapper mapper;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(header) Long userId, @PathVariable Long itemId) {
        log.info("Get item with id {} from user with id {}", itemId, userId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(header) Long userId) {
        log.info("Get list of all items from user with id {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getByRequest(@RequestParam String text, @RequestHeader(header) Long userId) {
        log.info("Search items with request {} from user with id {}", text, userId);
        return itemService.getByRequest(text, userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(header) Long userId, @Valid @RequestBody ItemDto itemDto) {
        Item item = mapper.makeModel(itemDto, userId);
        log.info("Create item {} from user with id {}", item, userId);
        return mapper.makeDto(itemService.create(item), null);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(header) Long userId, @RequestBody CreateCommentDto commentDto,
                                    @PathVariable Long itemId) {
        log.info("Create comment {} for item with id {} from user with id {}", commentDto, itemId, userId);
        return itemService.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(header) Long userId, @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        Item item = mapper.makeModel(itemDto, userId);
        log.info("Update item {} from user with id {}", item, userId);
        item.setId(itemId);
        return itemService.update(item);
    }
}
