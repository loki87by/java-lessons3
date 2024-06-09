package org.example.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BookingService {
    BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> getBookingList(Long userId) {
        return bookingRepository.findRequests(userId);
    }

    public String getStatus(Long userId, Long bookingId) {
        return bookingRepository.getStatus(userId, bookingId);
    }

    public String confirm(Long ownerId,
                          Long bookingId,
                          boolean confirm) {
        return bookingRepository.confirm(ownerId, bookingId, confirm);
    }

    public String book(Long userId,
                       Long itemId,
                       Instant startDate,
                       Instant endDate) {
        Long id = bookingRepository.book(userId, itemId, startDate, endDate);

        if (id != null) {
            return "Заявка зарегистрирована, статус можно посмотреть по адресу:\nhttp://localhost:8080/"+id;
        } else {
            return "Что-то пошло не так.";
        }
    }

    public String delete(Long userId,
                         Long bookingId) {
        return bookingRepository.remove(userId, bookingId);
    }
}













