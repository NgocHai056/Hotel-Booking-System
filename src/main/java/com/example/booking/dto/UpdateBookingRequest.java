package com.example.booking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookingRequest {
    private String bookingNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
