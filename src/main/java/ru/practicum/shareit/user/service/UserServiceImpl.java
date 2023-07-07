package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserStorageImpl userStorage;

    @Override
    public User create(User user) {
        if (userStorage.isContains(user.getEmail())) {
            throw new UserAlreadyExistException("User already exist");
        }
        return userStorage.create(user).orElseThrow(() -> new UserNotFoundException("Can't create the user"));
    }

    @Override
    public User update(User user) {
        return userStorage.update(user).orElseThrow(() -> new ItemNotFoundException("Can't update the user"));
    }

    @Override
    public User getById(Long userId) {
        return userStorage.findById(userId).orElseThrow(() -> new ItemNotFoundException("There is no user with this ID"));
    }

    @Override
    public List<User> getAll() {
        return userStorage.findAll();
    }

    @Override
    public void delete(Long userId) {
        userStorage.delete(userId);
    }
}
