package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    @Query("select b from bookings b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 ")
    List<Booking> findByBookerIdCurrent(Long userId, LocalDateTime now);

    List<Booking> findBookingByItemOwnerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemOwnerId(Long bookerId, Sort sort);

    @Query("select b from bookings b " +
            "where b.item.ownerId = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc")
    List<Booking> findBookingsByItemOwnerIdCurrent(Long userId, LocalDateTime now);

    List<Booking> findBookingByItemIdAndStartBefore(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemIdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);

    @Query("select b from bookings b " +
            " where b.item.id = ?1 " +
            " and b.booker.id = ?2" +
            " and b.end < ?3")
    List<Booking> findBookingsForAddComments(Long itemId, Long userId, LocalDateTime now);
}
