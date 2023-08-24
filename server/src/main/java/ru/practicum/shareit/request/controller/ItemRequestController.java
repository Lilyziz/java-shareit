package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.request.dto.PostResponseRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.service.IItemRequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final IItemRequestService requestService;
    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{requestId}")
    public RequestWithItemsDto getById(@RequestHeader(header) Long userId, @PathVariable Long requestId) {
        log.info("Get request with id {} from user with id {}", requestId, userId);
        return requestService.getById(requestId, userId);
    }

    @GetMapping
    public List<RequestWithItemsDto> getAllByUserId(@RequestHeader(header) Long userId) {
        log.info("Get list of requests from user with id {}", userId);
        return requestService.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestWithItemsDto> getAll(@RequestHeader(header) Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "20") int size) {
        log.info("Get list of requests from user with id {}", userId);
        return requestService.getAll(from, size, userId);
    }

    @PostMapping
    public PostResponseRequestDto create(@RequestHeader(header) Long userId,
                                         @RequestBody PostRequestDto postRequestDto) {
        log.info("Create request {} from user with id {}", postRequestDto, userId);
        return requestService.create(postRequestDto, userId);
    }
}
