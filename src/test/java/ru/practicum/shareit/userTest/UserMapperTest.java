package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {
    private User user;
    private UserMapper mapper;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "user", "user@user.user");
    }

    @Test
    public void makeDtoTest() {
        UserDto dto = mapper.makeDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    public void makeModelTest() {
        UserDto dto = new UserDto(1L, "user", "user@user.user");
        User fromDtoUser = mapper.makeModel(dto, 1L);

        assertEquals(dto.getId(), fromDtoUser.getId());
        assertEquals(dto.getName(), fromDtoUser.getName());
        assertEquals(dto.getEmail(), fromDtoUser.getEmail());
    }

    @Test
    public void makeDtoListTest() {
        List<User> users = List.of(user);
        List<UserDto> dtoList = mapper.makeDtoList(users);

        assertNotNull(dtoList);
        assertEquals(users.get(0).getId(), dtoList.get(0).getId());
        assertEquals(users.get(0).getName(), dtoList.get(0).getName());
        assertEquals(users.get(0).getEmail(), dtoList.get(0).getEmail());
    }
}
