package org.example.booking;

import org.example.item.Item;
import org.example.item.ItemInMemoryRepository;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Repository
public class BookingInMemoryRepository implements BookingRepository {
    ItemInMemoryRepository itemInMemoryRepository;
    BookingMapper bookingMapper;
    Utils utils;
    private final HashMap<Long, Booking> bookings = new HashMap<>();

    @Autowired
    public BookingInMemoryRepository(ItemInMemoryRepository itemInMemoryRepository, Utils utils, BookingMapper bookingMapper) {
        this.itemInMemoryRepository = itemInMemoryRepository;
        this.utils = utils;
        this.bookingMapper = bookingMapper;
    }

    private Item checkOwnersItem(Long userId, Booking booking) {
        List<Item> userItems = itemInMemoryRepository.findByUserId(userId);
        return userItems.stream().filter(i -> Objects.equals(i.getId(), booking.getItemId())).toList().getFirst();
    }

    @Override
    public List<BookingDTO> findRequests(Long userId) {
        List<BookingDTO> dtos = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            Item item = checkOwnersItem(userId, booking);

            if (item != null) {
                dtos.add(bookingMapper.toDTO(booking));
            }
        }
        return dtos;
    }

    @Override
    public void checkPrivate(Long userId, Long bookingId, String errorText) {
        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            throw new NoSuchElementException("Не найдено записей о бронировании с таким id.");
        }

        if (!Objects.equals(booking.getUser(), userId)) {
            throw new SecurityException(errorText);
        }
    }

    @Override
    public String getStatus(Long userId, Long bookingId) {
        checkPrivate(userId, bookingId, "Нельзя посмотреть статус чужой заявки.");
        Booking booking = bookings.get(bookingId);
        BookingStatus confirmState = booking.getState();
        int confirmStateIndex = confirmState.ordinal();

        if (confirmStateIndex == 1) {
            return "Ваша заявка одобрена.";
        } else if (confirmStateIndex == 2) {
            return "Ваша заявка отклонена.";
        } else {
            return "Ваша заявка на рассмотрении.";
        }
    }

    @Override
    public String confirm(Long ownerId,
                          Long bookingId,
                          boolean confirm) {
        Booking booking = bookings.get(bookingId);
        Item item = checkOwnersItem(ownerId, booking);

        if (item != null) {
            item.setBooked(confirm);
            itemInMemoryRepository.save(item);
            booking.setState(confirm ? BookingStatus.values()[1] : BookingStatus.values()[2]);
            return confirm ? "Заявка одобрена" : "Заявка отклонена";
        }
        throw new SecurityException("У вас нет прав для данной операции.");
    }

    @Override
    public Long book(Long userId,
                     Long itemId,
                     Instant startDate,
                     Instant endDate) {
        Item item = itemInMemoryRepository.findAll().get(itemId);
        int size = bookings.size();
        Long hash = Math.abs(Long.parseLong(String.valueOf(bookings.hashCode())));
        Long id = utils.getUniqueId(bookings, hash);
        Booking booking = new Booking(id,
                userId,
                item.getId(),
                0,
                Timestamp.from(startDate),
                Timestamp.from(endDate),
                new Timestamp(Long.parseLong(String.valueOf(Instant.now()))));
        bookings.put(id, booking);
        int length = bookings.size();

        if (size < length) {
            return id;
        }
        return null;
    }

    @Override
    public Instant getTime(Long id, boolean isStartDate) {
        Booking booking = bookings.get(id);

        return isStartDate ? booking.getStartDate() : booking.getEndDate();
    }

    @Override
    public String update(Long bookingId,
                         Instant startDate,
                         Instant endDate) {
        Booking booking = bookings.get(bookingId);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        return "Данные обновлены.";
    }

    @Override
    public String remove(Long bookingId) {
        Booking booking = bookings.get(bookingId);

        if (booking != null) {
            bookings.remove(bookingId);
            return "Заявка отменена.";
        }
        throw new NoSuchElementException("Запрашиваемый ресурс не найден.");
    }
}
