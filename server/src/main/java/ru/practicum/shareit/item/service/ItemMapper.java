package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemMapper {
    public static ItemDto makeDto(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(BookingMapper.bookingInItemDto(lastBooking));
        dto.setNextBooking(BookingMapper.bookingInItemDto(nextBooking));
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDetailedDtoList(comments));
        }
        return dto;
    }

    public static ItemDto makeDto(Item item, List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDetailedDtoList(comments));
        }
        dto.setRequestId(item.getRequestId());
        return dto;
    }

    public static Item makeModel(ItemDto itemDto, Long ownerId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        item.setOwnerId(ownerId);
        return item;
    }

    public static ItemInRequestDto makeRequestItemDto(Item item) {
        ItemInRequestDto dto = new ItemInRequestDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        dto.setOwner(item.getOwnerId());
        return dto;
    }

    public static List<ItemInRequestDto> makeRequestItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::makeRequestItemDto)
                .collect(Collectors.toList());
    }
}
