package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private IUserService userService;
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "user", "user@user.user");
    }

    @Test
    public void createTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User newUser = userService.create(user);

        assertNotNull(newUser);
        assertEquals(1, newUser.getId());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getEmail(), newUser.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateTest() {
        user.setName("updated name");
        user.setEmail("updated@user.user");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        User output = userService.update(user);

        assertNotNull(output);
        assertEquals(output.getId(), 1);
        assertEquals(output.getName(), user.getName());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getByIdTest() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        User user = userService.getById(1L);

        assertNotNull(user);
        assertEquals(1, user.getId());

        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void getByIdWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    public void getAllTest() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getId(), users.get(0).getId());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void deleteByIdTest() {
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
