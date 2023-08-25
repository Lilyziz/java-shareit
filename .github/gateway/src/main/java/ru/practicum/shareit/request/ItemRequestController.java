package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestClient requestClient;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class})
                                         @RequestBody PostRequestDto postRequestDto,
                                         @RequestHeader(header) Long userId) {
        log.info("Create request {} from user with id {}", postRequestDto, userId);
        return requestClient.create(postRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(header) Long userId) {
        log.info("Get list of requests from user with id {}", userId);
        return requestClient.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "20") @Min(0) int size,
                                         @RequestHeader(header) Long userId) {
        log.info("Get list of requests from user with id {}", userId);
        return requestClient.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId,
                                          @RequestHeader(header) Long userId) {
        log.info("Get request with id {} from user with id {}", requestId, userId);
        return requestClient.getById(requestId, userId);
    }
}
