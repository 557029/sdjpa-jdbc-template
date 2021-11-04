package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorExtractor implements ResultSetExtractor<Author> {
    @Override
    public Author extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        return new AuthorMapper().mapRow(resultSet, 0);
    }
}
