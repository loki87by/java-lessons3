package book;

import org.example.author.AuthorService;
import org.example.book.BookService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTestWithAnnotations {

    @Mock
    AuthorService mockAuthorService;

    @Test
    void testCreateBookDescriptionWithMockito() {
        BookService bookService = new BookService();
        bookService.setAuthorService(mockAuthorService);

        Mockito
                .when(mockAuthorService.getAuthorDescription(Mockito.anyInt()))
                .thenReturn("великий русский писатель");

        String bookDescription = bookService
                .createBookDescription("Война и мир", 1898, 5, "Л.Н.Толстой");

        Assertions.assertEquals("Война и мир, 1898 автор Л.Н.Толстой, великий русский писатель.",
                bookDescription);
    }
}
