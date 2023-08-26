package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.request.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAll(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(Long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> create(long userId, ItemInRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> update(ItemInRequestDto requestDto, Long itemId, long userId) {
        return patch("/" + itemId, userId, requestDto);
    }

    public ResponseEntity<Object> deleteItem(Long itemId) {
        return delete("/" + itemId);
    }

    public ResponseEntity<Object> getByRequest(String text, long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(Long itemId, long userId, CommentDto requestDto) {
        return post("/" + itemId + "/comment", userId, requestDto);
    }
}
