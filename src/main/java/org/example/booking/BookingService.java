package org.example.booking;

import org.example.user.UserRepository;
import org.example.utils.DateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService {
    BookingRepository bookingRepository;
    UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public List<BookingDTO> getBookingList(Long userId) {
        return bookingRepository.findRequests(userId);
    }

    public String getStatus(Long bookingId, Long userId) {
        return bookingRepository.getStatus(userId, bookingId);
    }

    public String confirm(Long ownerId,
                          Long bookingId,
                          boolean confirm) {
        return bookingRepository.confirm(ownerId, bookingId, confirm);
    }

    public void checkDates(Instant startDate, Instant endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Дата начала не должна быть позже даты окончания.");
        }

        if (startDate.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Дата начала не должна быть раньше текущего времени.");
        }
    }

    public String book(Long userId,
                       Long itemId,
                       String startString,
                       String endString) {
        boolean isRealUser = userRepository.checkUser(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }
        Instant startDate = DateConverter.convertToInstant(startString);
        Instant endDate = DateConverter.convertToInstant(endString);
        checkDates(startDate, endDate);
        Long id = bookingRepository.book(userId, itemId, startDate, endDate);

        if (id != null) {
            return "Заявка зарегистрирована, статус можно посмотреть по адресу:\nhttp://localhost:8080/booking/" + id;
        }
        throw new RuntimeException("Что-то пошло не так.");
    }

    public String update(Long userId,
                         Long bookingId,
                         String startString,
                         String endString) {
        bookingRepository.checkPrivate(userId, bookingId, "Нельзя править чужие заявки.");

        if (startString.isEmpty() && endString.isEmpty()) {
            throw new IllegalArgumentException("Изменений не обнаружено.");
        }
        Instant startDate = startString.isEmpty() ?
                bookingRepository.getTime(bookingId, true) :
                DateConverter.convertToInstant(startString);
        Instant endDate = endString.isEmpty() ?
                bookingRepository.getTime(bookingId, false) :
                DateConverter.convertToInstant(endString);
        checkDates(startDate, endDate);

        return bookingRepository.update(bookingId, startDate, endDate);
    }

    public String delete(Long userId,
                         Long bookingId) {
        bookingRepository.checkPrivate(userId, bookingId, "Нет прав для удаления чужой заявки.");
        return bookingRepository.remove(bookingId);
    }
}