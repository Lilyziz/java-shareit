package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.dto.PostRequestDto;
import ru.practicum.shareit.request.dto.PostResponseRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestMapper {

    public static Request toModel(PostRequestDto dto, Long requester) {
        Request request = new Request();
        request.setDescription(dto.getDescription());
        request.setRequesterId(requester);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public static PostResponseRequestDto toPostResponseDto(Request request) {
        PostResponseRequestDto dto = new PostResponseRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static RequestWithItemsDto toRequestWithItemsDto(Request request, List<Item> items) {
        List<ItemInRequestDto> itemDto = ItemMapper.makeRequestItemDtoList(items);
        RequestWithItemsDto dto = new RequestWithItemsDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(itemDto);
        return dto;
    }

    public static List<RequestWithItemsDto> toRequestWithItemsDtoList(List<Request> requests,
                                                                      ItemRepository itemRepository) {
        List<RequestWithItemsDto> result = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            for (Request request : requests) {
                List<Item> items = itemRepository.findAllByRequestId(request.getId());
                RequestWithItemsDto requestDto = RequestMapper.toRequestWithItemsDto(request, items);
                result.add(requestDto);
            }
        }
        return result;
    }

    public static List<RequestWithItemsDto> toRequestWithItemsDtoList(Page<Request> requests,
                                                                      ItemRepository repository) {
        return requests.stream()
                .map((Request request) -> {
                    List<Item> items = repository.findAllByRequestId(request.getId());
                    return RequestMapper.toRequestWithItemsDto(request, items);
                }).collect(Collectors.toList());
    }
}
