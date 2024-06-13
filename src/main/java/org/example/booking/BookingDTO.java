package org.example.booking;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BookingDTO {
    private Long id;
    private String startDate;
    private String endDate;
    private BookingStatus state;
    private Timestamp createdDate;
}
