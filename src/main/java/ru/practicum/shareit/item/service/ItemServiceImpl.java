package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorageImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements IItemService {
    private final ItemStorageImpl itemStorage;
    private final UserStorageImpl userStorage;

    @Override
    public Item create(Item item) {
        if (isOwnerExist(item)) {
            return itemStorage.create(item).orElseThrow(() -> new ItemNotFoundException("Can't create the item"));
        } else throw new OwnerNotFoundException("There is no owner with this ID: " + item.getOwner());
    }

    @Override
    public Item update(Item item) {
        if (isOwner(item)) {
            return itemStorage.update(item).orElseThrow(() -> new ItemNotFoundException("Can't update the item"));
        } else throw new OwnerNotFoundException("Only owner can update the item");

    }

    @Override
    public Item getById(Long itemId) {
        return itemStorage.findById(itemId).orElseThrow(() -> new ItemNotFoundException("There is no item with this ID"));
    }

    @Override
    public List<Item> getAll(Long userId) {
        return itemStorage.findAll(userId);
    }

    @Override
    public List<Item> getByRequest(String request) {
        if (!request.isEmpty()) {
            return itemStorage.findByRequest(request);
        } else return new ArrayList<>();
    }

    private boolean isOwnerExist(Item item) {
        List<User> users = userStorage.findAll();
        List<User> result = users.stream().filter(user -> item.getOwner().equals(user.getId())).collect(Collectors.toList());
        return !result.isEmpty();
    }

    private boolean isOwner(Item item) {
        return itemStorage.findById(item.getId()).get().getOwner().equals(item.getOwner());
    }
}
