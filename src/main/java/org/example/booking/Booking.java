package org.example.booking;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false, updatable = false)
    private Instant startDate;
    @Column(name = "end_date", nullable = false)
    private Instant endDate;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @Column(name = "item_id", nullable = false, updatable = false)
    private Long itemId;
    @Column(name = "booking_STATE", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus state;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdDate;

    public Booking(long id,
                   long user,
                   long itemId,
                   int bookingState,
                   Timestamp startDate,
                   Timestamp endDate,
                   Timestamp createdDate) {
        this.id = id;
        this.startDate = startDate.toInstant();
        this.endDate = endDate.toInstant();
        this.ownerId = user;
        this.itemId = itemId;
        this.state = BookingStatus.values()[bookingState];
        this.createdDate = createdDate;
    }

    public Booking(long user,
                   long itemId,
                   int bookingState,
                   Timestamp startDate,
                   Timestamp endDate) {
        this.startDate = startDate.toInstant();
        this.endDate = endDate.toInstant();
        this.ownerId = user;
        this.itemId = itemId;
        this.state = BookingStatus.values()[bookingState];
    }
}
