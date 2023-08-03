package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM items i WHERE (lower(i.name) LIKE concat('%', ?1, '%') OR " +
            "lower(i.description) LIKE concat('%', ?1, '%')) AND i.available = true ")
    List<Item> search(String request);

    List<Item> findAllByOwnerIdOrderByIdAsc(Long userId);
}
