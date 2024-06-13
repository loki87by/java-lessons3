package org.example.booking;

//import org.example.utils.DateConverter;
import org.springframework.stereotype.Service;

@Service
public class BookingMapper {

    public BookingDTO toDTO (Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setState(booking.getState());
        dto.setCreatedDate(booking.getCreatedDate());
        dto.setStartDate(booking.getStartDate().toString());
        dto.setEndDate(booking.getEndDate().toString());
        dto.setId(booking.getId());
        return dto;
    }

/*    public Booking toModel (BookingDTO dto, Long user, Long itemId) {
        Booking booking = new Booking();
        booking.setItemId(itemId);
        booking.setUser(user);
        booking.setId(dto.getId());
        booking.setStartDate(DateConverter.convertToInstant(dto.getStartDate()));
        booking.setEndDate(DateConverter.convertToInstant(dto.getEndDate()));
        booking.setBooked(dto.isBooked());
        booking.setBookingConfirm(dto.getBookingConfirm());
        return booking;
    }*/
}