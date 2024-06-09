package org.example.booking;

import org.springframework.stereotype.Service;

@Service
public class BookingMapper {

    public BookingDTO toDTO (Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBooked(booking.isBooked());
        dto.setBookingConfirm(booking.getBookingConfirm());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setId(booking.getId());
        return dto;
    }

    public Booking toModel (BookingDTO dto, Long user, Long itemId) {
        Booking booking = new Booking();
        booking.setItemId(itemId);
        booking.setUser(user);
        booking.setId(dto.getId());
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setBooked(dto.isBooked());
        booking.setBookingConfirm(dto.getBookingConfirm());
        return booking;
    }
}