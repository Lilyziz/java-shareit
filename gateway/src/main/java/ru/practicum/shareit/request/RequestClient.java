package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.PostRequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(PostRequestDto postRequestDto, Long userId) {
        return post("", userId, postRequestDto);
    }

    public ResponseEntity<Object> getAllByUserId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAll(int from, int size, Long userId) {
        Map<String, Object> parameters = Map.of("from", from, "size", size);

        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(Long requestId, Long userId) {
        return get("/" + requestId, userId);
    }
}