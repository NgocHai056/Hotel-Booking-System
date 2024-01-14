package com.example.booking.controller;

import com.example.booking.common.BaseResponse;
import com.example.booking.common.RequestMappingVersionConstants;
import com.example.booking.dto.CreateBookingRequest;
import com.example.booking.dto.UpdateBookingRequest;
import com.example.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "booking" + RequestMappingVersionConstants.VERSION)
@RequestMapping(value = RequestMappingVersionConstants.VERSION_PATH + "/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody CreateBookingRequest createBookingRequest) throws Exception {
        BaseResponse<Object> response = bookingService.createBooking(createBookingRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@RequestBody UpdateBookingRequest createBookingRequest) throws Exception {
        BaseResponse<Object> response = bookingService.updateBooking(createBookingRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(value = "/{bookingNumber}/delete", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable("bookingNumber") String bookingNumber) throws Exception {
        BaseResponse<Object> response = bookingService.cancelBooking(bookingNumber);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
