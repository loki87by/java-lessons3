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
}