package book;

import org.example.author.AuthorService;
import org.example.author.MockAuthorService;
import org.example.book.BookService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.SQLException;

public class TestBookService {

    @Test
    void testCreateBookDescriptionWithMock() throws SQLException {
        BookService bookService = new BookService();
        AuthorService mockAuthorService = Mockito.mock(AuthorService.class);
        bookService.setAuthorService(mockAuthorService);

        Mockito
                .when(mockAuthorService.getAuthorDescription(Mockito.anyInt()))
                .thenReturn("знаменитый русский писатель");

        String bookDescription = bookService
                .createBookDescription("Война и мир", 1898, 5, "Л.Н.Толстой");

        Assertions.
                assertEquals("Война и мир, 1898 автор Л.Н.Толстой, знаменитый русский писатель.",
                        bookDescription);

    }

    @Test
    void testCreateBookDescription() {
        BookService bookService = new BookService();
        AuthorService mockAuthorService = new MockAuthorService();
        bookService.setAuthorService(mockAuthorService);

        String bookDescription = bookService
                .createBookDescription("Война и мир", 1898, 5, "Л.Н.Толстой");

        Assertions.
                assertEquals("Война и мир, 1898 автор Л.Н.Толстой, знаменитый русский писатель.",
                        bookDescription);
    }

    @Test
    void testFindPublicationYear(int bookId) {
        BookDao mockBookDao = Mockito.mock(BookDao.class);
        String creationDate = mockBookDao.findPublicationDate(bookId);
        DateService mockDateService = Mockito.mock(DateService.class);
        int publicationYear = mockDateService.getYear(creationDate);

        /*if(publicationYear < 1700) {
            throw new IllegalArgumentException("Год слишком маленький: где-то произошла ошибка.");
        }
        Assertions.*/
    }

    @Test
    void testCreateBookDescriptionComplexLogic() throws SQLException {
        BookService bookService = new BookService();
        AuthorService mockAuthorService = Mockito.mock(AuthorService.class);
        bookService.setAuthorService(mockAuthorService);

        Mockito
                .when(mockAuthorService.getAuthorDescription(Mockito.anyInt()))
                .thenAnswer(invocationOnMock -> {
                    int authorId = invocationOnMock.getArgument(0, Integer.class);

                    if(authorId % 2 == 0) {
                        return "великий русский писатель";
                    } else {
                        return "великий английский писатель";
                    }
                });
        String bookDescriptionEnglish =
                bookService.createBookDescription("Гамлет",
                        1599,
                        11,
                        "Уильям Шекспир");

        Assertions.assertEquals("Гамлет, 1599 автор Уильям Шекспир, великий английский писатель.",
                bookDescriptionEnglish);

        String bookDescriptionRussian =
                bookService.createBookDescription("Война и мир",
                        1898,
                        5,
                        "Л.Н.Толстой");

        Assertions.assertEquals("Война и мир, 1898 автор Л.Н.Толстой, великий русский писатель.",
                bookDescriptionRussian);
    }

    @Test
    void testCreateBookDescriptionWhenException() {
        BookService bookService = new BookService();
        AuthorService mockAuthorService = Mockito.mock(AuthorService.class);
        bookService.setAuthorService(mockAuthorService);

        Mockito
                .when(mockAuthorService.getAuthorDescription(Mockito.anyInt()))
                .thenThrow(new DataNotAvailableException("Ошибка при доступе к базе"));

        String bookDescription = bookService
                .createBookDescription("Война и мир", 1898, 5, "Л.Н.Толстой");

        Assertions.assertEquals("Война и мир, 1898", bookDescription);
    }
}
