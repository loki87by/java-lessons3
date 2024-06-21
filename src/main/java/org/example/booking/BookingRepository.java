package org.example.booking;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.Instant;
import java.util.List;

@RepositoryRestResource
public interface BookingRepository {

    List<BookingDTO> findRequests(Long userId, String state);

    List<BookingDTO> findRequestsWithState(Long userId, String state);

    void checkPrivate(Long userId, Long bookingId, String errorText);

    String getStatus(Long userId, Long bookingId);

    String confirm(Long ownerId,
                   Long bookingId,
                   boolean confirm);

    Long book(Long userId,
              Long itemId,
              Instant startDate,
              Instant endDate);

    Instant getTime(Long id, boolean isStartDate);

    String update(Long bookingId,
                  Instant startDate,
                  Instant endDate);

    String remove(Long bookingId);
}
