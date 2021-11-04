package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorDaoImpl implements AuthorDao {
    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        final String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.title, book.publisher from author " +
                "left outer join book on author.id = book.author_id where author.id = ?";
        return this.jdbcTemplate.query(sql, new AuthorExtractor(), id);
    }

    @Override
    public Author getByName(String firstName, String lastName) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ? AND last_name = ?",
                getRowMapper(),
                firstName,
                lastName);

    }

    @Override
    public Author saveNewAuthor(Author author) {

        this.jdbcTemplate.update("INSERT INTO author(first_name, last_name) VALUES (?, ?)",
                author.getFirstName(), author.getLastName());
        final Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        assert id != null;
        return this.getById(id);
    }

    @Override
    public Author updateAuthor(Author author) {
        this.jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(),
                author.getLastName(),
                author.getId()
        );
        return this.getById(author.getId());

    }

    @Override
    public void deleteAuthorById(Long id) {
        this.jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }
}
