package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("Get user {}", id);
        return userClient.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto requestDto) {
        log.info("Creating user");
        return userClient.create(requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserDto requestDto,
                                         @PathVariable Long id) {
        log.info("Update user {}", id);
        return userClient.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        log.info("Delete user {}", id);
        return userClient.deleteById(id);
    }
}
