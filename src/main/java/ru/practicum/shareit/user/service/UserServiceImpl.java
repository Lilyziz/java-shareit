package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.CreateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserStorageImpl userStorage;

    @Override
    public User create(User user) throws CreateException {
        if (userStorage.isContains(user.getEmail())) {
            throw new AlreadyExistException("User already exist");
        }
        return userStorage.create(user);
    }

    @Override
    public User update(User user) throws NotFoundException {
        return userStorage.update(user);
    }

    @Override
    public User getById(Long userId) {
        return userStorage.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with this ID"));
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
