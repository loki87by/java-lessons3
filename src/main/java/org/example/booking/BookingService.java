package org.example.booking;

import org.example.user.UserInMemoryRepository;
import org.example.utils.DateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService {
    BookingInMemoryRepository bookingInMemoryRepository;
    UserInMemoryRepository userInMemoryRepository;

    @Autowired
    public BookingService(BookingInMemoryRepository bookingInMemoryRepository, UserInMemoryRepository userInMemoryRepository) {
        this.bookingInMemoryRepository = bookingInMemoryRepository;
        this.userInMemoryRepository = userInMemoryRepository;
    }

    public List<BookingDTO> getBookingList(Long userId) {
        return bookingInMemoryRepository.findRequests(userId);
    }

    public String getStatus(Long bookingId, Long userId) {
        return bookingInMemoryRepository.getStatus(userId, bookingId);
    }

    public String confirm(Long ownerId,
                          Long bookingId,
                          boolean confirm) {
        return bookingInMemoryRepository.confirm(ownerId, bookingId, confirm);
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
        boolean isRealUser = userInMemoryRepository.checkUser(userId);

        if (!isRealUser) {
            throw new NoSuchElementException("Пользователь с id=" + userId + " не найден. Проверьте заголовки запроса.");
        }
        Instant startDate = DateConverter.convertToInstant(startString);
        Instant endDate = DateConverter.convertToInstant(endString);
        checkDates(startDate, endDate);
        Long id = bookingInMemoryRepository.book(userId, itemId, startDate, endDate);

        if (id != null) {
            return "Заявка зарегистрирована, статус можно посмотреть по адресу:\nhttp://localhost:8080/booking/" + id;
        }
        throw new RuntimeException("Что-то пошло не так.");
    }

    public String update(Long userId,
                         Long bookingId,
                         String startString,
                         String endString) {
        bookingInMemoryRepository.checkPrivate(userId, bookingId, "Нельзя править чужие заявки.");

        if (startString.isEmpty() && endString.isEmpty()) {
            throw new IllegalArgumentException("Изменений не обнаружено.");
        }
        Instant startDate = startString.isEmpty() ?
                bookingInMemoryRepository.getTime(bookingId, true) :
                DateConverter.convertToInstant(startString);
        Instant endDate = endString.isEmpty() ?
                bookingInMemoryRepository.getTime(bookingId, false) :
                DateConverter.convertToInstant(endString);
        checkDates(startDate, endDate);

        return bookingInMemoryRepository.update(bookingId, startDate, endDate);
    }

    public String delete(Long userId,
                         Long bookingId) {
        bookingInMemoryRepository.checkPrivate(userId, bookingId, "Нет прав для удаления чужой заявки.");
        return bookingInMemoryRepository.remove(bookingId);
    }
}