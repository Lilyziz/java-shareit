package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Create user {}", userDto);
        return userClient.create(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@NotNull(message = ("userID is null"))
                                                   @Min(1) @PathVariable Long userId) {
        log.info("Get user with id {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get list of all users");
        return userClient.getAll();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@NotNull(message = "userID is null") @Min(1) @PathVariable Long userId,
                                             @Validated({Update.class}) @RequestBody UserDto userDto) {
        log.info("Update user {}", userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@NotNull(message = ("userID is null")) @Min(1) @PathVariable Long userId) {
        log.info("Delete user with id {}", userId);
        userClient.deleteById(userId);
    }
}
