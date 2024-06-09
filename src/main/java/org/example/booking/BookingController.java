package org.example.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    //need test all down there
    @PostMapping("")
    public String book(@RequestHeader("X-Later-User-Id") Long userId,
                       @RequestParam("itemId") Long itemId,
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  @RequestParam("startDate") Instant startDate,
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  @RequestParam("endDate") Instant endDate) {
        return bookingService.book(userId, itemId, startDate, endDate);
    }

    @PostMapping("/{bookingId}")
    public String confirm(@RequestHeader("X-Later-User-Id") Long ownerId,
                          @PathVariable Long bookingId,
                          @RequestParam("confirm") boolean confirm) {
        return bookingService.confirm(ownerId, bookingId, confirm);
    }

    //need add upd method

    @DeleteMapping("")
    public String delete(@RequestHeader("X-Later-User-Id") Long userId,
                         @RequestParam("bookingId") Long bookingId) {
        return bookingService.delete(userId, bookingId);
    }
}






