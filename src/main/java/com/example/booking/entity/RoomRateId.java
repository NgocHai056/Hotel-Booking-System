package com.example.booking.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


@Data
public class RoomRateId implements Serializable {

    private Long roomType;
    private Long ratePlan;
    private LocalDate date;

}
