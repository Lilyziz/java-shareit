package ru.practicum.shareit.item.storage;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemStorageImpl implements IItemStorage {
    private long id = 1;
    private final Map<Long, Item> items = new HashMap<>();

    private long generateId() {
        return id++;
    }

    @Override
    public Optional<Item> create(Item item) {
        long id = generateId();
        item.setId(id);
        items.put(id, item);
        return Optional.of(item);
    }

    @Override
    public Optional<Item> update(Item item) {
        long itemId = item.getId();
        if (!items.containsKey(itemId)) {
            return Optional.empty();
        }
        Item updatingItem = items.get(itemId);
        updateItem(updatingItem, item);
        return Optional.of(updatingItem);
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findAll(Long userId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Item> findByRequest(String request) {
        List<Item> requestItems = new ArrayList<>();
        request = request.toLowerCase();
        for (Item item : items.values()) {
            if ((StringUtils.containsOnly(request, item.getName().toLowerCase())
                    || StringUtils.containsOnly(request, item.getDescription().toLowerCase()))
                    && BooleanUtils.isTrue(item.getAvailable())) {
                requestItems.add(item);
            }
        }
        return requestItems;
    }

    private void updateItem(Item updatingItem, Item item) {
        Optional.ofNullable(item.getName()).ifPresent(updatingItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(updatingItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(updatingItem::setAvailable);
    }
}
