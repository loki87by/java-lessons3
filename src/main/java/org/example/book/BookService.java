package org.example.book;

import org.example.author.AuthorService;

import java.sql.SQLException;

public class BookService {
    private AuthorService authorService;
    private BookDao bookDao;
    private DateService dateService;
    private int leastExpectedYear = 1700;

    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    public int findPublicationYear(int bookId) {
        String creationDate = bookDao.findPublicationDate(bookId);
        DateService complexDateService = new DateService();
        this.dateService = complexDateService;
        int publicationYear = complexDateService.getYear(creationDate);

        if(publicationYear < leastExpectedYear) {
            throw new IllegalArgumentException("Год слишком маленький: где-то произошла ошибка.");
        }
        return publicationYear;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public String createBookDescription(String bookName, int creationYear, int authorId, String authorName) {
        StringBuffer description = new StringBuffer();
        description.append(bookName).append(", ");
        description.append(creationYear);

        try {
            String authorDescription = authorService.getAuthorDescription(authorId);

            if (authorDescription != null) {
                description.append(" автор ");
                description.append(authorName).append(", ");
                description.append(authorDescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  description.toString();
    }
}
