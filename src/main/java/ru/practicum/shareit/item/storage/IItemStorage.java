package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface IItemStorage {
    Optional<Item> create(Item item);

    Optional<Item> update(Item item);

    Optional<Item> findById(Long itemId);

    List<Item> findAll(Long userId);

    List<Item> findByRequest(String request);
}
