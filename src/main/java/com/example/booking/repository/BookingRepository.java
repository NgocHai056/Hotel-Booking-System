package com.example.booking.repository;

import com.example.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query("SELECT r FROM Booking r WHERE r.bookingNumber = :bookingNumber")
    public Booking findByBookingNumber(String bookingNumber);
}
