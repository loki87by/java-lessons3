package org.example.booking;

import org.example.item.Item;
import org.example.item.ItemRepository;
import org.example.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class BookingRepository {
    ItemRepository itemRepository;
    BookingMapper bookingMapper;
    Utils utils;
    private final HashMap<Long, Booking> bookings = new HashMap<>();

    @Autowired
    public BookingRepository(ItemRepository itemRepository, Utils utils, BookingMapper bookingMapper) {
        this.itemRepository = itemRepository;
        this.utils = utils;
        this.bookingMapper = bookingMapper;
    }

    private Item checkOwnersItem(Long userId, Booking booking) {
        List<Item> userItems = itemRepository.findByUserId(userId);
        return userItems.stream().filter(i -> Objects.equals(i.getId(), booking.getItemId())).toList().getFirst();
    }

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

    public void checkPrivate(Long userId, Long bookingId, String errorText) {
        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            throw new NoSuchElementException("Не найдено записей о бронировании с таким id.");
        }

        if (!Objects.equals(booking.getUser(), userId)) {
            throw new SecurityException(errorText);
        }
    }

    public String getStatus(Long userId, Long bookingId) {
        checkPrivate(userId, bookingId, "Нельзя посмотреть статус чужой заявки.");
        Booking booking = bookings.get(bookingId);
        int confirmState = booking.getBookingConfirm();

        if (confirmState == 1) {
            return "Ваша заявка одобрена.";
        } else if (confirmState == -1) {
            return "Ваша заявка отклонена.";
        } else {
            return "Ваша заявка на рассмотрении.";
        }
    }

    public String confirm(Long ownerId,
                          Long bookingId,
                          boolean confirm) {
        Booking booking = bookings.get(bookingId);
        Item item = checkOwnersItem(ownerId, booking);

        if (item != null && booking.isBooked()) {
            item.setBooked(confirm);
            itemRepository.save(item);
            booking.setBookingConfirm(confirm ? 1 : -1);
            return confirm ? "Заявка одобрена" : "Заявка отклонена";
        }
        throw new SecurityException("У вас нет прав для данной операции.");
    }

    public Long book(Long userId,
                     Long itemId,
                     Instant startDate,
                     Instant endDate) {
        Item item = itemRepository.findAll().get(itemId);
        int size = bookings.size();
        Long hash = Math.abs(Long.parseLong(String.valueOf(bookings.hashCode())));
        Long id = utils.getUniqueId(bookings, hash);
        Booking booking = new Booking();
        booking.setId(id);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setUser(userId);
        booking.setItemId(item.getId());
        booking.setBooked(true);
        bookings.put(id, booking);
        int length = bookings.size();

        if (size < length) {
            return id;
        }
        return null;
    }

    public Instant getTime(Long id, boolean isStartDate) {
        Booking booking = bookings.get(id);

        return isStartDate ? booking.getStartDate() : booking.getEndDate();
    }

    public String update(Long bookingId,
                         Instant startDate,
                         Instant endDate) {
        Booking booking = bookings.get(bookingId);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        return "Данные обновлены.";
    }

    public String remove(Long bookingId) {
        Booking booking = bookings.get(bookingId);

        if (booking != null) {
            bookings.remove(bookingId);
            return "Заявка отменена.";
        }
        throw new NoSuchElementException("Запрашиваемый ресурс не найден.");
    }
}
