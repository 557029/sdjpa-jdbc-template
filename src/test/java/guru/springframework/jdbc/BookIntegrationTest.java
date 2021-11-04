package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
public class BookIntegrationTest {
    @Autowired
    private BookDao bookDao;

    @Test
    @Order(1)
    public void testGetById() {
        //Book getById(Long id);
        final Book book = bookDao.getById(1L);
        assert book != null;
    }

    @Test
    @Order(2)
    public void testGetByTitle() {
        // Book getByTitle(String title);
        final Book book = bookDao.getByTitle("Clean Code");
        assert book != null;
    }

    @Test
    @Order(3)
    public void testSaveNewBook() {
        //Book saveNewBook(Book book);
        final Book book = new Book();
        book.setTitle("Test");
        book.setPublisher("Test");
        book.setIsbn("978-0321125217");
        book.setAuthorId(3L);
        final Book resBook = this.bookDao.saveNewBook(book);
        assert resBook != null;
        assert resBook.getId() != null;
    }

    @Test
    @Order(4)
    public void testUpdateBook() {
        // Book updateBook(Book book);
        final Book book = new Book();
        book.setTitle("Test");
        book.setPublisher("Test");
        book.setIsbn("978-0321125217");
        book.setAuthorId(3L);
        final Book savedBook = this.bookDao.saveNewBook(book);
        assert savedBook.getPublisher().equals("Test");

        book.setPublisher("Privet");
        final Book updatedBook = this.bookDao.updateBook(book);
        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookDao.getById(updatedBook.getId());
        });

    }

    @Test
    @Order(5)
    public void testDeleteBookById() {
        // void deleteBookById(Long id);
    }

}
