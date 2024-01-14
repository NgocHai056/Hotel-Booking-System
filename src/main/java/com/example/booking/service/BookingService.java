package com.example.booking.service;

import com.example.booking.common.BaseResponse;
import com.example.booking.common.JmsMessageHandler;
import com.example.booking.dto.CreateBookingRequest;
import com.example.booking.dto.UpdateBookingRequest;
import com.example.booking.entity.*;
import com.example.booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;

    @Autowired
    private RoomRateRepository roomRateRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    public BaseResponse<Object> cancelBooking(String bookingNumber) {
        BaseResponse<Object> response = new BaseResponse<>();

        Booking booking = bookingRepository.findByBookingNumber(bookingNumber);

        if (booking == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Chưa có lịch đặt cho phòng hiện tại");
            return response;
        }

        RoomAvailability roomAvailable = isRoomAvailable(booking.getRoomType().getRoomTypeId(), booking.getCheckInDate()).get(0);

        roomAvailable.setAvailableRooms(roomAvailable.getAvailableRooms() + 1);
        roomAvailabilityRepository.save(roomAvailable);

        bookingRepository.delete(booking);

        return response;
    }

    public BaseResponse<Object> updateBooking(UpdateBookingRequest request) {
        BaseResponse<Object> response = new BaseResponse<>();

        Booking booking = bookingRepository.findByBookingNumber(request.getBookingNumber());

        if (booking == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Chưa có lịch đặt cho phòng hiện tại");
            return response;
        }

        RoomAvailability roomAvailabilitie = isRoomAvailable(booking.getRoomType().getRoomTypeId(), booking.getCheckInDate()).get(0);

        roomAvailabilitie.setAvailableRooms(roomAvailabilitie.getAvailableRooms() + 1);
        roomAvailabilityRepository.save(roomAvailabilitie);

        List<RoomAvailability> roomAvailabilities = isRoomAvailable(booking.getRoomType().getRoomTypeId(), request.getCheckInDate());
        if (roomAvailabilities.isEmpty() || roomAvailabilities.get(0).getAvailableRooms() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Không còn phòng chống");
            return response;
        }
        RoomAvailability roomAvailabilityAfter = roomAvailabilities.get(0);
        roomAvailabilityAfter.setAvailableRooms(roomAvailabilityAfter.getAvailableRooms() - 1);
        roomAvailabilityRepository.save(roomAvailabilityAfter);

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());

        response.setData(bookingRepository.save(booking));
        return response;
    }

    public BaseResponse<Object> createBooking(CreateBookingRequest request) {
        BaseResponse<Object> response = new BaseResponse<>();

        Optional<Guest> guest = guestRepository.findById(request.getGuestId());

        if (guest.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Khách hàng không tồn tại");
            return response;
        }

        Optional<Hotel> hotel = hotelRepository.findById(request.getHotelId());
        if (hotel.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Khách sạn không tồn tại.");
            return response;
        }

        List<RoomAvailability> roomAvailabilities = isRoomAvailable(request.getRoomTypeId(), request.getCheckInDate());
        if (roomAvailabilities.isEmpty() || roomAvailabilities.get(0).getAvailableRooms() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Không còn phòng chống");
            return response;
        }

        RoomAvailability roomAvailability = roomAvailabilities.get(0);

        roomAvailability.setAvailableRooms(roomAvailability.getAvailableRooms() - 1);

        // 2. Calculate Total Price
        List<RoomRate> roomRate = calculateTotalPrice(request.getRoomTypeId(), request.getRatePlanId(), request.getCheckInDate());

        if (roomRate.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError("Chưa có kế hoạch giá cho phòng hiện tại.");
            return response;
        }

        roomAvailabilityRepository.save(roomAvailabilities.get(0));

        BigDecimal totalPrice = roomRate.get(0).getPrice();

        // 4. Create Booking
        Booking newBooking = new Booking();
        newBooking.setBookingNumber("BOOKING" + String.format("%04d", bookingRepository.count() + 1));

        newBooking.setHotel(hotel.get());
        newBooking.setRoomType(roomRate.get(0).getRoomType());
        newBooking.setRatePlan(roomRate.get(0).getRatePlan());

        newBooking.setCheckInDate(request.getCheckInDate());
        newBooking.setCheckOutDate(request.getCheckOutDate());

        newBooking.setPrice(totalPrice);
        newBooking.setGuest(guest.get());
        newBooking.setStatus("CONFIRMED");
        newBooking.setNotes("");

        response.setData(bookingRepository.save(newBooking));

        // 5. Update room avaibilities
        this.sendMessage(JmsMessageHandler.convertObjectToJson(roomAvailabilities.get(0).getRoomType()));

        return response;

    }

    private List<RoomAvailability> isRoomAvailable(Long roomTypeId, LocalDate date) {
        return roomAvailabilityRepository.findByRoomTypeAndDate(roomTypeId, date);
    }

    private List<RoomRate> calculateTotalPrice(Long roomTypeId, Long ratePlanId, LocalDate date) {
        List<RoomRate> roomRates = roomRateRepository.findByRoomTypeIdAndRatePlanIdAndDate(roomTypeId, ratePlanId, date);
        return roomRates;
    }

    public void sendMessage(String message) {

        jmsTemplate.convertAndSend("Inventory.Topic", message);

    }
}
