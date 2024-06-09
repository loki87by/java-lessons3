package org.example.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("")
    public List<BookingDTO> getList(@RequestHeader("X-Later-User-Id") Long userId) {
        return bookingService.getBookingList(userId);
    }

    @GetMapping("/{bookingId}")
    public String getStatus(@RequestHeader("X-Later-User-Id") Long userId,
                            @PathVariable(name = "bookingId") Long bookingId) {
        return bookingService.getStatus(bookingId, userId);
    }

    @PostMapping("")
    public String confirm(@RequestHeader("X-Later-User-Id") Long ownerId,
                          @RequestParam Long bookingId,
                          @RequestParam boolean confirm) {
        return bookingService.confirm(ownerId, bookingId, confirm);
    }

    @PatchMapping("")
    public String book(@RequestHeader("X-Later-User-Id") Long userId, @RequestBody Long itemId,
                       @RequestParam Instant startDate,
                       @RequestParam Instant endDate) {
        return bookingService.book(userId, itemId, startDate, endDate);
    }

    @DeleteMapping("")
    public String delete(@RequestHeader("X-Later-User-Id") Long userId,
                         @RequestParam Long bookingId) {
        return bookingService.delete(userId, bookingId);
    }
}






