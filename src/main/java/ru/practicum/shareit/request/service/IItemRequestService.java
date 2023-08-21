package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.request.dto.PostResponseRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;

import java.util.List;

public interface IItemRequestService {
    PostResponseRequestDto create(PostRequestDto dto, Long userId);

    List<RequestWithItemsDto> getAllByUserId(Long userId);

    List<RequestWithItemsDto> getAll(int from, int size, Long userId);

    RequestWithItemsDto getById(Long requestId, Long userId);
}
