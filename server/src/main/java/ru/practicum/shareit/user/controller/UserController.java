package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final IUserService userService;
    private final UserMapper mapper;

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("Get user with id {}", userId);
        return mapper.makeDto(userService.getById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Get list of all users");
        return mapper.makeDtoList(userService.getAll());
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        User user = mapper.makeModel(userDto, null);
        log.info("Create user {}", user);
        return mapper.makeDto(userService.create(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        User user = mapper.makeModel(userDto, userId);
        log.info("Update user {}", user);
        return mapper.makeDto(userService.update(user));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Delete user with id {}", userId);
        userService.deleteById(userId);
    }
}
