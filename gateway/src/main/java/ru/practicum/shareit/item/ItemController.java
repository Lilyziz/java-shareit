package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemClient itemClient;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(header) Long userId, @PathVariable Long itemId) {
        log.info("Get item with id {} from user with id {}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(header) Long userId) {
        log.info("Get list of all items from user with id {}", userId);
        return itemClient.getAll(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByRequest(@RequestParam String text, @RequestHeader(header) Long userId) {
        log.info("Search items with request {} from user with id {}", text, userId);
        return itemClient.getByRequest(text, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item {} from user with id {}", itemDto, userId);
        return itemClient.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(header) Long userId,
                                                @RequestBody CreateCommentDto commentDto, @PathVariable Long itemId) {
        log.info("Create comment {} for item with id {} from user with id {}", commentDto, itemId, userId);
        return itemClient.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(header) Long userId, @RequestBody ItemDto itemDto,
                                         @PathVariable Long itemId) {
        log.info("Update item {} from user with id {}", itemDto, userId);
        return itemClient.update(itemDto, itemId, userId);
    }
}
