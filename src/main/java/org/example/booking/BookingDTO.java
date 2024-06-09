package org.example.booking;

import lombok.Data;

import java.time.Instant;

@Data
public class BookingDTO {
    private Long id;
    private Instant startDate;
    private Instant endDate;
    private boolean isBooked;
    private int bookingConfirm;
}
