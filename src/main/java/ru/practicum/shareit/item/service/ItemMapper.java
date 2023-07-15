package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemMapper {
    public static ItemDto makeDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item makeModel(ItemDto itemDto, Long ownerId) {
        return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }

    public List<ItemDto> makeDtoList(List<Item> userItems) {
        return userItems.stream().map(ItemMapper::makeDto).collect(Collectors.toList());
    }
}
