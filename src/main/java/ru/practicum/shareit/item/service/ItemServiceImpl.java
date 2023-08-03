package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements IItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;

    @Override
    @Transactional
    public Item create(Item item) throws InternalServerErrorException {
        if (!isOwnerExist(item)) {
            throw new NotFoundException("There is no owner with this ID");
        }
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public ItemDto update(Item item) throws NotFoundException {
        if (!isOwner(item)) {
            throw new NotFoundException("Only owner can update the item");
        }

        List<Comment> comments = commentRepository.findByItemId(item.getId());
        Item updatingItem = updateItem(item);
        item = itemRepository.save(updatingItem);

        return mapper.makeDto(item, comments);
    }

    @Override
    @Transactional
    public ItemDto getById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("There is no item with this ID"));
        List<Comment> comments = commentRepository.findByItemId(itemId);

        if (item.getOwnerId().equals(userId)) {
            return constructItemDtoForOwner(item, LocalDateTime.now(), comments);
        }
        return ItemMapper.makeDto(item, null, null, comments);
    }

    @Override
    @Transactional
    public CommentDto createComment(CreateCommentDto dto, Long itemId, Long userId) {
        if (dto.getText().isBlank() || dto.getText().isEmpty()) {
            throw new BadRequestException("");
        }

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("There is no item with this ID"));
        User author = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("There is no user with this ID"));

        if (bookingRepository.findBookingsForAddComments(itemId, userId, LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("");
        }
        Comment comment = CommentMapper.toModel(dto, item, author);
        comment = commentRepository.save(comment);

        return CommentMapper.toCommentDetailedDto(comment);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("There is no user with this ID: " + userId));

        List<ItemDto> result = new ArrayList<>();
        fillItemDtoList(result, itemRepository.findAllByOwnerIdOrderByIdAsc(userId), userId);

        return result;
    }

    @Override
    public List<ItemDto> getByRequest(String request, Long userId) {
        if (request.isBlank() || request.isEmpty()) {
            return Collections.emptyList();
        }
        List<ItemDto> result = new ArrayList<>();
        List<Item> foundItems = itemRepository.search(request.toLowerCase());
        fillItemDtoList(result, foundItems, userId);

        return result;
    }

    private boolean isOwnerExist(Item item) {
        List<User> users = userRepository.findAll();
        List<User> result = users.stream().filter(user -> user.getId().equals(item.getOwnerId()))
                .collect(Collectors.toList());

        return !result.isEmpty();
    }

    private boolean isOwner(Item item) {
        Optional<Item> checkItem = itemRepository.findById(item.getId());
        return checkItem.map(value -> value.getOwnerId().equals(item.getOwnerId())).orElse(false);
    }

    private Item updateItem(Item item) {
        Item updatingItem = itemRepository.findById(item.getId()).orElseThrow();
        Optional.ofNullable(item.getName()).ifPresent(updatingItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(updatingItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(updatingItem::setAvailable);
        return updatingItem;
    }

    private ItemDto constructItemDtoForOwner(Item item, LocalDateTime now, List<Comment> comments) {
        Booking lastBooking = bookingRepository.findBookingByItemIdAndStartBefore(
                item.getId(), now, Sort.by("start").descending())
                .stream().findFirst().orElse(null);

        Booking nextBooking = bookingRepository.findBookingByItemIdAndStartAfter(
                item.getId(), now, Sort.by("start").ascending())
                .stream().filter(e -> !e.getStatus().equals(BookingStatus.REJECTED)).findFirst().orElse(null);

        return ItemMapper.makeDto(item, lastBooking, nextBooking, comments);
    }

    private void fillItemDtoList(List<ItemDto> targetList, List<Item> foundItems, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        for (Item item : foundItems) {
            List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
            if (item.getOwnerId().equals(userId)) {
                ItemDto dto = constructItemDtoForOwner(item, now, comments);
                targetList.add(dto);
            } else {
                targetList.add(ItemMapper.makeDto(item, comments));
            }
        }
    }
}
