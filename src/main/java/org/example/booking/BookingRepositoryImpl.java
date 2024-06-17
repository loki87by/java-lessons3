package org.example.booking;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.item.Item;
import org.example.item.ItemJPARepository;
import org.example.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Repository
public class BookingRepositoryImpl implements BookingRepository{
    private final EntityManager entityManager;
    private final BookingMapper mapper;
    private final Utils utils;
    private final BookingJPARepository bookingJPARepository;
    private final ItemJPARepository itemJPARepository;
    @Autowired
    public BookingRepositoryImpl(EntityManager entityManager,
                                 BookingMapper mapper,
                                 Utils utils,
                                 ItemJPARepository itemJPARepository,
                                 BookingJPARepository bookingJPARepository) {
        this.entityManager=entityManager;
        this.mapper = mapper;
        this.bookingJPARepository = bookingJPARepository;
        this.utils = utils;
        this.itemJPARepository = itemJPARepository;
    }

    private Booking getBookingById(Long id) {
        return bookingJPARepository.findById(id).orElse(null);
    }

    @Override
    public List<BookingDTO> findRequests(Long ownerId) {
        List<Item> userItems = itemJPARepository.findItemsByOwnerIdAndBookedIsTrue(ownerId);
        List<Long> userItemsIds = userItems.stream().map(Item::getId).toList();
        List<Booking> books = bookingJPARepository.findAllByItemIdIn(userItemsIds);
        return utils.getListDTO(books, mapper::toDTO);
    }

    @Override
    public void checkPrivate(Long userId, Long bookingId, String errorText) {
        Booking booking = bookingJPARepository.findById(bookingId).orElse(null);

        if (booking == null) {
            throw new NoSuchElementException("Не найдено записей о бронировании с таким id.");
        }

        if (!Objects.equals(booking.getOwnerId(), userId)) {
            throw new SecurityException(errorText);
        }
    }

    @Override
    public String getStatus(Long userId, Long bookingId) {
        checkPrivate(userId, bookingId, "Нельзя посмотреть статус чужой заявки.");
        Booking booking = bookingJPARepository.findById(bookingId).orElse(null);

        if(booking == null) {
            throw new NoSuchElementException("Не найдено записей о бронировании с таким id.");
        }
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

    private Item checkOwnersItem(Long userId, Booking booking) {
        List<Item> userItems = itemJPARepository.findAllByOwnerIdIs(userId);
        return userItems.stream().filter(i -> Objects.equals(i.getId(), booking.getItemId())).toList().getFirst();
    }

    @Override
    @Transactional
    public String confirm(Long ownerId, Long bookingId, boolean confirm) {
        Booking booking = bookingJPARepository.findById(bookingId).orElse(null);

        if(booking == null) {
            throw new NoSuchElementException("Не найдено записей о бронировании с таким id.");
        }
        Item item = checkOwnersItem(ownerId, booking);

        if (item != null) {
            booking.setState(confirm ? BookingStatus.values()[1] : BookingStatus.values()[2]);
            entityManager.merge(booking);
            return confirm ? "Заявка одобрена" : "Заявка отклонена";
        }
        throw new SecurityException("У вас нет прав для данной операции.");
    }

    @Override
    @Transactional
    public Long book(Long userId, Long itemId, Instant startDate, Instant endDate) {
        Item item = itemJPARepository.getReferenceById(itemId);
        item.setBooked(true);
        entityManager.persist(item);
        Booking booking = new Booking(userId,
                item.getId(),
                0,
                Timestamp.from(startDate),
                Timestamp.from(endDate));
        entityManager.persist(booking);
        return booking.getId();
    }

    @Override
    public Instant getTime(Long id, boolean isStartDate) {
        Booking booking = getBookingById(id);
        return isStartDate ? booking.getStartDate() : booking.getEndDate();
    }

    @Override
    @Transactional
    public String update(Long bookingId, Instant startDate, Instant endDate) {
        Booking booking = getBookingById(bookingId);
        BookingStatus state = booking.getState();

        if(state != BookingStatus.WAITING) {
            throw new SecurityException("Заявка рассмотрена владельцем и не подлежит корректировкам.");
        }
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        entityManager.merge(booking);
        return "Данные обновлены.";
    }

    @Override
    @Transactional
    public String remove(Long bookingId) {
        Booking booking = getBookingById(bookingId);

        if (booking != null) {
            Long itemId = booking.getItemId();
            Item item = itemJPARepository.getReferenceById(itemId);
            item.setBooked(false);
            entityManager.persist(item);
            entityManager.remove(booking);
            return "Заявка отменена.";
        }
        throw new NoSuchElementException("Запрашиваемый ресурс не найден.");
    }
}
