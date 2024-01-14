package com.example.booking;

import com.example.booking.common.BaseResponse;
import com.example.booking.dto.CreateBookingRequest;
import com.example.booking.entity.Booking;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingApplicationTests {

    @Autowired
    private BookingService bookingService;

    @Test
    void contextLoads() {

    }

    @Test
    public void createBooking_validRequest_bookingCreated() {

        CreateBookingRequest validRequest = new CreateBookingRequest();

        validRequest.setHotelId(1L);
        validRequest.setRoomTypeId(2L);
        validRequest.setRatePlanId(1L);
        validRequest.setGuestId(10L);
        validRequest.setCheckInDate(LocalDate.of(2024, 1, 15));
        validRequest.setCheckOutDate(LocalDate.of(2024, 1, 20));


        assertNull(bookingService.createBooking(validRequest).getData());
    }

    @Test
    public void createBooking_unavailableRoomType() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setHotelId(1L);
        request.setRatePlanId(1L);
        request.setGuestId(10L);
        request.setCheckInDate(LocalDate.of(2024, 1, 15));
        request.setCheckOutDate(LocalDate.of(2024, 1, 20));

        request.setRoomTypeId(0L);

        BaseResponse<Object> response = bookingService.createBooking(request);

        assertNull(response.getData());
        assertEquals(400, response.getStatus());
    }

    @Test
    public void createBooking_invalidGuestInformation() {
        CreateBookingRequest zeroGuestsRequest = new CreateBookingRequest();
        zeroGuestsRequest.setHotelId(1L);
        zeroGuestsRequest.setRoomTypeId(2L);
        zeroGuestsRequest.setRatePlanId(1L);
        zeroGuestsRequest.setCheckInDate(LocalDate.of(2024, 1, 15));
        zeroGuestsRequest.setCheckOutDate(LocalDate.of(2024, 1, 20));

        zeroGuestsRequest.setGuestId(0L);

        BaseResponse<Object> response = bookingService.createBooking(zeroGuestsRequest);

        assertNull(response.getData());
        assertEquals(400, response.getStatus());
    }

}
