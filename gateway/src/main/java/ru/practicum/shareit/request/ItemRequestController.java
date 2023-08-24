package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Min;

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
        return requestClient.create(postRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(header) Long userId) {
        return requestClient.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "20") @Min(0) int size,
                                         @RequestHeader(header) Long userId) {
        return requestClient.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId,
                                          @RequestHeader(header) Long userId) {
        return requestClient.getById(requestId, userId);
    }
}
