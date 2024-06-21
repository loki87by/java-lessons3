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
import java.util.*;
import java.util.stream.Collectors;

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

    private BookingStatus parseBookingStatus(String status) {
        return switch (status.toUpperCase()) {
            case "WAITING" -> BookingStatus.WAITING;
            case "APPROVED" -> BookingStatus.APPROVED;
            case "REJECTED" -> BookingStatus.REJECTED;
            default -> null;
        };
    }

    private List<BookingDTO> getDtoList(List<Booking> bookings, String state) {
        List<Booking> books = new ArrayList<>();

        if (state.equalsIgnoreCase("ALL")) {
            books = bookings;
        } else if (state.equalsIgnoreCase("PAST")) {
            Instant today = Instant.now();
            books = bookings.stream()
                    .filter(booking -> booking.getEndDate().isBefore(today))
                    .toList();
        } else if (state.equalsIgnoreCase("FUTURE")) {
            Instant today = Instant.now();
            books = bookings.stream()
                    .filter(booking -> booking.getStartDate().isAfter(today))
                    .toList();
        } else if (state.equalsIgnoreCase("CURRENT")) {
            Instant today = Instant.now();
            books = bookings.stream()
                    .filter(booking -> (booking.getStartDate().isBefore(today) || booking.getStartDate().equals(today))
                            && (booking.getEndDate().isAfter(today) || booking.getEndDate().equals(today)))
                    .collect(Collectors.toList());
        } else {
            BookingStatus status = parseBookingStatus(state);
            if (status != null) {
                books = bookings.stream()
                        .filter(booking -> booking.getState() == status)
                        .collect(Collectors.toList());
            }
        }

        List<Booking> sortedBooks = books.stream()
                .sorted(Comparator.comparing(Booking::getCreatedDate))
                .toList();
        return utils.getListDTO(sortedBooks, mapper::toDTO);
    }

    @Override
    public List<BookingDTO> findRequests(Long ownerId, String state) {
        List<Item> userItems = itemJPARepository.findItemsByOwnerIdAndBookedIsTrue(ownerId);
        //System.out.println("\u001B[38;5;44m" + "userItems: "+userItems.toString()+ "\u001B[0m");
        List<Long> userItemsIds = userItems.stream().map(Item::getId).toList();
        List<Booking> books = bookingJPARepository.findAllByItemIdIn(userItemsIds);
        return getDtoList(books, state);
    }

    @Override
    public List<BookingDTO> findRequestsWithState(Long ownerId, String state) {
        List<Booking> userBookings = bookingJPARepository.findAllByOwnerId(ownerId);
        return getDtoList(userBookings, state);
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

    private Item checkOwnersItem(Long userId, Booking booking) {
        List<Item> userItems = itemJPARepository.findAllByOwnerIdIs(userId);
        return userItems.stream().filter(i -> Objects.equals(i.getId(), booking.getItemId())).toList().getFirst();
    }

    private boolean isItemOwner(Long ownerId, Long bookingId) {
        Booking booking = bookingJPARepository.findById(bookingId).orElse(null);

        if(booking == null) {
            throw new NoSuchElementException("Не найдено записей о бронировании с таким id.");
        }
        Item item = checkOwnersItem(ownerId, booking);
        return item != null;
    }

    @Override
    public String getStatus(Long userId, Long bookingId) {
        if (!isItemOwner(userId, bookingId)) {
            checkPrivate(userId, bookingId, "Нельзя посмотреть статус чужой заявки.");
        }
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

    @Override
    @Transactional
    public String confirm(Long ownerId, Long bookingId, boolean confirm) {
        Booking booking = bookingJPARepository.findById(bookingId).orElse(null);

        if (isItemOwner(ownerId, bookingId) && booking != null) {
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
