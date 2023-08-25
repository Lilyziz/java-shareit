package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<Request, Long> {
    List<Request> findRequestByRequesterId(Long requester, Sort sort);

    @Query("SELECT r FROM requests r WHERE r.requesterId <> ?1")
    Page<Request> findAllByRequesterId(Long userId, Pageable pageable);
}
