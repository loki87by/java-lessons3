package org.example.booking;

import lombok.Data;

import java.time.Instant;

@Data
public class Booking {
    private Long id;
    private Instant startDate;
    private Instant endDate;
    private Long user;
    private Long itemId;
    private boolean isBooked;
    private int bookingConfirm = 0;
}
