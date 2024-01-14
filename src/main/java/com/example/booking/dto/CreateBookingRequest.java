package com.example.booking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {

    private Long hotelId;
    private Long roomTypeId;
    private Long ratePlanId;
    private Long guestId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
