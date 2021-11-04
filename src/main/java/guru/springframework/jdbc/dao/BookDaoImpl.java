package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookDaoImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?",
                getRowMapper(),
                id);
    }

    @Override
    public Book getByTitle(String title) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM book WHERE title = ?",
                getRowMapper(),
                title);
    }

    @Override
    public Book saveNewBook(Book book) {
        this.jdbcTemplate.update("INSERT INTO book(title, isbn, publisher, author_id) VALUES (?,?,?,?)",
                book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId());
        final Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        assert id != null;
        return this.getById(id);
    }

    @Override
    public Book updateBook(Book book) {
        this.jdbcTemplate.update("UPDATE book SET title = ?, isbn = ?, publisher = ?, author_id = ? WHERE id = ?",
                book.getTitle(),
                book.getIsbn(),
                book.getPublisher(),
                book.getAuthorId(),
                book.getId()
        );
        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        this.jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    private RowMapper<Book> getRowMapper() {
        return new BookMapper();
    }
}
