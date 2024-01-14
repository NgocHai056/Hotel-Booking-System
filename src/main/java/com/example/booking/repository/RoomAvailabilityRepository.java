package com.example.booking.repository;

import com.example.booking.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

    @Query("SELECT r FROM RoomAvailability r WHERE r.roomType.roomTypeId = :roomTypeId AND r.date BETWEEN :startDate AND :endDate")
    List<RoomAvailability> findByRoomTypeIdAndDateBetween(Long roomTypeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM RoomAvailability r WHERE r.roomType.roomTypeId = :roomTypeId AND r.date = :date")
    List<RoomAvailability> findByRoomTypeAndDate(Long roomTypeId, LocalDate date);
}
