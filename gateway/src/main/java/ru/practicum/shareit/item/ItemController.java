package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private final String header = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(header) Long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all items from user {}", userId);
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id,
                                          @RequestHeader(header) Long userId) {
        log.info("Get item {}", id);
        return itemClient.getById(id, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) Long userId,
                                         @RequestBody @Valid ItemInRequestDto requestDto) {
        log.info("Create item");
        return itemClient.create(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ItemInRequestDto requestDto,
                                         @PathVariable Long id,
                                         @RequestHeader(header) Long userId) {
        log.info("Update item {}", id);
        return itemClient.update(requestDto, id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByRequest(@RequestParam String text, @RequestHeader(header) Long userId) {
        log.info("Search items by text {}", text);
        return itemClient.getByRequest(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId, @RequestHeader(header) Long userId,
                                                @Valid @RequestBody CommentDto requestDto) {
        log.info("Create comment to item {}", itemId);
        return itemClient.createComment(itemId, userId, requestDto);
    }
}
