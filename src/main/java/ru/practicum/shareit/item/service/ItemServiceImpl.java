package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CreateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorageImpl;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements IItemService {
    private final ItemStorageImpl itemStorage;
    private final UserStorageImpl userStorage;

    @Override
    public Item create(Item item) throws CreateException {
        if (!isOwnerExist(item)) {
            throw new NotFoundException("There is no owner with this ID");
        }
        return itemStorage.create(item);
    }

    @Override
    public Item update(Item item) throws NotFoundException {
        if (!isOwner(item)) {
            throw new NotFoundException("Only owner can update the item");
        }
        return itemStorage.update(item);
    }

    @Override
    public Item getById(Long itemId) {
        return itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("There is no item with this ID"));
    }

    @Override
    public List<Item> getAll(Long userId) {
        return itemStorage.findAll(userId);
    }

    @Override
    public List<Item> getByRequest(String request) {
        return request.isEmpty()
                ? new ArrayList<>()
                : itemStorage.findByRequest(request);
    }

    private boolean isOwnerExist(Item item) {
        return userStorage.findAll().stream()
                .anyMatch(user -> item.getOwner().equals(user.getId()));
    }

    private boolean isOwner(Item item) {
        return itemStorage.findById(item.getId()).isPresent()
                ? itemStorage.findById(item.getId()).get().getOwner().equals(item.getOwner())
                : false;
    }
}
