package com.example.booking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Entity
@Table(name = "room_rates")
@IdClass(RoomRateId.class)
public class RoomRate {

    @Id
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Id
    @ManyToOne
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

}
