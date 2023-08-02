package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static UserDto makeDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User makeModel(UserDto userDto, Long userId) {
        return new User(userId, userDto.getName(), userDto.getEmail());
    }

    public static List<UserDto> makeDtoList(List<User> users) {
        return users.stream().map(UserMapper::makeDto).collect(Collectors.toList());
    }
}
