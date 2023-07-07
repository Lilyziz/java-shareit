package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserStorage {
    Optional<User> create(User user);

    Optional<User> update(User user);

    Optional<User> findById(Long userId);

    List<User> findAll();

    void delete(Long userId);
}
