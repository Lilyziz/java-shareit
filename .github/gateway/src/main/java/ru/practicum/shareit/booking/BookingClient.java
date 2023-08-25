package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/bookings"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingPostDto dto, Long userId) {
        if (!isStartBeforeEnd(dto)) {
            throw new IllegalArgumentException("Invalid time " +
                    "start: " + dto.getStart() + " end: " + dto.getEnd() + " now: ");
        }
        return post("", userId, dto);
    }

    public ResponseEntity<Object> update(Long bookingId, Boolean approved, Long userId) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }

    public ResponseEntity<Object> getById(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllByBooker(Long userId, String state, int from, int size) {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);

        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByItemOwnerId(Long userId, String state, int from, int size) {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);

        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    private boolean isStartBeforeEnd(BookingPostDto dto) {
        return dto.getStart().isBefore(dto.getEnd());
    }
}

