package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.request.dto.PostResponseRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements IItemRequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;
    private final RequestMapper mapper;
    private final Sort sortByCreatedDesc = Sort.by("created").descending();

    @Override
    public RequestWithItemsDto getById(Long requestId, Long userId) {
        userCheck(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("There is no request with this ID: " + requestId));

        List<Item> items = itemRepository.findAllByRequestId(requestId);

        return RequestMapper.toRequestWithItemsDto(request, items);
    }

    @Override
    public List<RequestWithItemsDto> getAllByUserId(Long userId) {
        userCheck(userId);

        List<Request> requests = requestRepository.findRequestByRequesterId(userId, sortByCreatedDesc);

        return mapper.toRequestWithItemsDtoList(requests, itemRepository);
    }

    @Override
    public List<RequestWithItemsDto> getAll(int from, int size, Long userId) {
        userCheck(userId);

        Pageable pageable = PageRequest.of(from, size, sortByCreatedDesc);
        Page<Request> requests = requestRepository.findAllByRequesterId(userId, pageable);

        return mapper.toRequestWithItemsDtoList(requests, itemRepository);
    }

    @Override
    @Transactional
    public PostResponseRequestDto create(PostRequestDto dto, Long userId) {
        userCheck(userId);

        Request request = mapper.toModel(dto, userId);
        request = requestRepository.save(request);

        return mapper.toPostResponseDto(request);
    }

    private void userCheck(long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("There is no user with this ID: " + userId));
    }
}
