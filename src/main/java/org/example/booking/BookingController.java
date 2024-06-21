package org.example.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    //просмотр заявок к пользователю
    @GetMapping("owner")
    public List<BookingDTO> getList(@RequestHeader("X-Later-User-Id") Long userId,
                                    @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingList(userId, state);
    }

    //просмотр заявок пользователя
    @GetMapping("")
    public List<BookingDTO> getListWithState(@RequestHeader("X-Later-User-Id") Long userId,
                                    @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingListWithState(userId, state);
    }

    //просмотр заявки оставленной пользователем
    @GetMapping("/{bookingId}")
    public String getStatus(@RequestHeader("X-Later-User-Id") Long userId,
                            @PathVariable(name = "bookingId") Long bookingId) {
        return bookingService.getStatus(bookingId, userId);
    }

    //бронирование
    @PostMapping("")
    public String book(@RequestHeader("X-Later-User-Id") Long userId,
                       @RequestParam("itemId") Long itemId,
                       @RequestParam("startDate") String startDate,
                       @RequestParam("endDate") String endDate) {
        return bookingService.book(userId, itemId, startDate, endDate);
    }

    //подтверждение брони владельцем
    @PostMapping("/{bookingId}")
    public String confirm(@RequestHeader("X-Later-User-Id") Long ownerId,
                          @PathVariable(name = "bookingId") Long bookingId,
                          @RequestParam("approved") boolean confirm) {
        return bookingService.confirm(ownerId, bookingId, confirm);
    }

    //изменение дат бронирования заказчиком
    @PatchMapping("/{bookingId}")
    public String update(@RequestHeader("X-Later-User-Id") Long userId,
                         @PathVariable(name = "bookingId") Long bookingId,
                         @RequestParam(name = "startDate", required = false, defaultValue = "") String startDate,
                         @RequestParam(name = "endDate", required = false, defaultValue = "") String endDate) {
        return bookingService.update(userId, bookingId, startDate, endDate);
    }

    //отмена брони
    @DeleteMapping("")
    public String delete(@RequestHeader("X-Later-User-Id") Long userId,
                         @RequestParam("bookingId") Long bookingId) {
        return bookingService.delete(userId, bookingId);
    }
}






