package org.example.booking;

import org.example.item.Item;
import org.example.item.ItemRepository;
import org.example.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public String getStatus(Long userId, Long bookingId) {
        Booking booking = bookings.get(bookingId);

        if (!Objects.equals(booking.getUser(), userId)) {
            return "Нельзя посмотреть статус чужой заявки.";
        } else {
            int confirmState = booking.getBookingConfirm();

            if (confirmState == 1) {
                return "Ваша заявка одобрена.";
            } else if (confirmState == -1) {
                return "Ваша заявка отклонена.";
            } else {
                return "Ваша заявка на рассмотрении.";
            }
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
        return "У вас нет прав для данной операции.";
    }

    public Long book(Long userId,
                     Long itemId,
                     Instant startDate,
                     Instant endDate) {
        Item item = itemRepository.findAll().get(itemId);
        int size = itemRepository.findAll().size();
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
        int length = itemRepository.findAll().size();

        if (size < length) {
            return id;
        }
        return null;
    }

    public String remove(Long userId, Long bookingId) {
        Booking booking = bookings.get(bookingId);
        Item item = checkOwnersItem(userId, booking);

        if (item != null) {
            bookings.remove(bookingId);
            return "Заявка отменена.";
        }
        return "У вас нет прав для данной операции.";
    }
}
