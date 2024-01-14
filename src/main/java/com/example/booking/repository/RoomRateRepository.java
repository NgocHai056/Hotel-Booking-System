package com.example.booking.repository;

import com.example.booking.entity.RoomRate;
import com.example.booking.entity.RoomRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRateRepository extends JpaRepository<RoomRate, RoomRateId> {
    @Query("SELECT r FROM RoomRate r WHERE r.roomType.roomTypeId = :roomTypeId AND r.date BETWEEN :startDate AND :endDate")
    public List<RoomRate> findByRoomTypeAndDate(Long roomTypeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM RoomRate r WHERE r.roomType.roomTypeId = :roomTypeId AND r.ratePlan.ratePlanId = :ratePlanId  AND r.date = :date")
    public List<RoomRate> findByRoomTypeIdAndRatePlanIdAndDate(Long roomTypeId, Long ratePlanId, LocalDate date);

}
