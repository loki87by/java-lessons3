package org.example.booking;

import lombok.Data;

@Data
public class BookingDTO {
    private Long id;
    private String startDate;
    private String endDate;
    private boolean isBooked;
    private int bookingConfirm;
}
