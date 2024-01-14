package com.example.booking.entity;

import javax.persistence.*;
import java.time.LocalDate;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Entity
@Table(name = "room_availabilities")
@IdClass(RoomAvailabilityId.class)
public class RoomAvailability {

    @Id
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "available_rooms", nullable = false)
    private int availableRooms;
}
