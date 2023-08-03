package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;
    private final ItemMapper mapper;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId, @RequestHeader(header) Long userId) {
        log.info("Get item with id: {}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(header) Long userId) {
        log.info("Get list of all items");
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getByRequest(@RequestParam String text, @RequestHeader(header) Long userId) {
        log.info("Search items with request: {}", text);
        return itemService.getByRequest(text, userId);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader(header) Long userId) {
        Item item = mapper.makeModel(itemDto, userId);
        log.info("Create item: {}", item.toString());
        return mapper.makeDto(itemService.create(item), null);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CreateCommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader(header) Long userId) {
        log.info("Create comment: {}", commentDto);
        return itemService.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId, @RequestHeader(header) Long userId) {
        Item item = mapper.makeModel(itemDto, userId);
        log.info("Update item: {}", item.toString());
        item.setId(itemId);
        return itemService.update(item);
    }
}
