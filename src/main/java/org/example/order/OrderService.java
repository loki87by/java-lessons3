package org.example.order;

import org.example.book.BookService;

import java.time.LocalDate;

public class OrderService {
    CustomerService customerService;
    BookService bookService;
    OrderDao orderDao;

    public void saveOrder(int customerId, int bookId, int amount, LocalDate orderDate) {

        if(customerService.customerIsBlocked(customerId)) {
            throw new CustomerBlockedException();
        }

        String customerAddress = customerService.getCustomerAddress(customerId);
        LocalDate deliveryDate = orderDate.plusDays(2);

        orderDao.saveOrder(customerId, customerAddress, bookId, amount, deliveryDate);
        bookService.decreaseAvailableAmount(bookId, amount);
    }
}
