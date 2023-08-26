package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.request.client.BaseClient;
import ru.practicum.shareit.request.dto.PostRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAll(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> create(long userId, PostRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getAllByUserId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getById(Long requestId, long userId) {
        return get("/" + requestId, userId);
    }
}
