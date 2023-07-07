package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface IItemService {
    Item create(Item item);

    Item update(Item item);

    Item getById(Long itemId);

    List<Item> getAll(Long userId);

    List<Item> getByRequest(String request);
}
