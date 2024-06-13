package org.example.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.item.Item;
import org.example.item.ItemInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class BookingRepositoryImpl implements BookingRepository{
    private final EntityManager entityManager;
    private final BookingMapper mapper;
    private final ItemInMemoryRepository itemInMemoryRepository;
    @Autowired
    public BookingRepositoryImpl(EntityManager entityManager, BookingMapper mapper, ItemInMemoryRepository itemInMemoryRepository) {
        this.entityManager=entityManager;
        this.mapper = mapper;
        this.itemInMemoryRepository = itemInMemoryRepository;
    }

/*    private List<Booking> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> root = cr.from(Booking.class);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }*/

    private Booking getBookingById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> root = cr.from(Booking.class);
        cr.select(root).where(cb.equal(root.get("id"), id));
        return entityManager.createQuery(cr).getSingleResult();
    }

    @Override
    public List<BookingDTO> findRequests(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> root = cr.from(Booking.class);
        cr.select(root).where(cb.equal(root.get("owner_id"), userId));
        List<Booking> foundBookings = entityManager.createQuery(cr).getResultList();
        return foundBookings
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void checkPrivate(Long userId, Long bookingId, String errorText) {
        Booking booking = getBookingById(bookingId);

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
        Booking booking = getBookingById(bookingId);
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


    //TODO: need update
    private Item checkOwnersItem(Long userId, Booking booking) {
        List<Item> userItems = itemInMemoryRepository.findByUserId(userId);
        return userItems.stream().filter(i -> Objects.equals(i.getId(), booking.getItemId())).toList().getFirst();
    }

    @Override
    public String confirm(Long ownerId, Long bookingId, boolean confirm) {
        Booking booking = getBookingById(bookingId);
        //TODO: need update
        Item item = checkOwnersItem(ownerId, booking);


        if (item != null) {
            //TODO: need update
            item.setBooked(confirm);
            //TODO: need update
            itemInMemoryRepository.save(item);
            booking.setState(confirm ? BookingStatus.values()[1] : BookingStatus.values()[2]);
            entityManager.merge(booking);
            return confirm ? "Заявка одобрена" : "Заявка отклонена";
        }
        throw new SecurityException("У вас нет прав для данной операции.");
    }

    @Override
    public Long book(Long userId, Long itemId, Instant startDate, Instant endDate) {
        //TODO: need update
        Item item = itemInMemoryRepository.findAll().get(itemId);
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
    public String update(Long bookingId, Instant startDate, Instant endDate) {
        Booking booking = getBookingById(bookingId);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        entityManager.merge(booking);
        return "Данные обновлены.";
    }

    @Override
    public String remove(Long bookingId) {
        Booking booking = getBookingById(bookingId);

        if (booking != null) {
            entityManager.remove(bookingId);
            return "Заявка отменена.";
        }
        throw new NoSuchElementException("Запрашиваемый ресурс не найден.");
    }
}
