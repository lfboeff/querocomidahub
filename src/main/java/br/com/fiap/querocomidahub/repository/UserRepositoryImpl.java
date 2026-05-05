package br.com.fiap.querocomidahub.repository;

import br.com.fiap.querocomidahub.domain.Address;
import br.com.fiap.querocomidahub.domain.User;
import br.com.fiap.querocomidahub.domain.UserType;
import br.com.fiap.querocomidahub.exception.DatabaseIntegrityException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<User> findAll() {
        return jdbcClient.sql(USER_WITH_ADDRESS_BASE_SELECT)
                .query(USER_ROW_MAPPER)
                .list();
    }

    @Override
    public List<User> findByName(String name) {
        return jdbcClient.sql(USER_WITH_ADDRESS_BASE_SELECT + "WHERE u.name LIKE :name")
                .param("name", "%" + name + "%")
                .query(USER_ROW_MAPPER)
                .list();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return jdbcClient.sql(USER_WITH_ADDRESS_BASE_SELECT + "WHERE u.login = :login")
                .param("login", login)
                .query(USER_ROW_MAPPER)
                .optional();
    }

    @Override
    public Long save(User user) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                        INSERT INTO users (type, name, email, login, password)
                        VALUES (:type, :name, :email, :login, :password)
                        """)
                .param("type", user.getType().name())
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .update(keyHolder);
        var key = keyHolder.getKey();
        if (key == null) {
            throw new DatabaseIntegrityException("Failed to retrieve generated key after saving user");
        }
        return key.longValue();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE email = :email")
                .param("email", email)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public boolean existsByLogin(String login) {
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE login = :login")
                .param("login", login)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public boolean existsByEmailForDifferentUser(String email, Long id) {
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE email = :email AND id != :id")
                .param("email", email)
                .param("id", id)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public boolean existsByLoginForDifferentUser(String login, Long id) {
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE login = :login AND id != :id")
                .param("login", login)
                .param("id", id)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public boolean existsById(Long id) {
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE id = :id")
                .param("id", id)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public void update(Long id, User user) {
        int rowsAffected = jdbcClient.sql("""
                        UPDATE users
                        SET type = :type, name = :name, email = :email, login = :login,
                            last_modified_at = CURRENT_TIMESTAMP
                        WHERE id = :id
                        """)
                .param("type", user.getType().name())
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("login", user.getLogin())
                .param("id", id)
                .update();
        if (rowsAffected != 1) {
            throw new DatabaseIntegrityException("Failed to update user");
        }
    }

    @Override
    public void delete(Long id) {
        int rowsAffected = jdbcClient.sql("DELETE FROM users WHERE id = :id")
                .param("id", id)
                .update();
        if (rowsAffected != 1) {
            throw new DatabaseIntegrityException("Failed to delete user");
        }
    }

    @Override
    public String findPasswordById(Long id) {
        return jdbcClient.sql("SELECT password FROM users WHERE id = :id")
                .param("id", id)
                .query(String.class)
                .single();
    }

    @Override
    public Optional<String> findPasswordByLogin(String login) {
        return jdbcClient.sql("SELECT password FROM users WHERE login = :login")
                .param("login", login)
                .query(String.class)
                .optional();
    }

    @Override
    public void updatePasswordById(Long id, String hashedPassword) {
        int rowsAffected = jdbcClient.sql("UPDATE users SET password = :password, last_modified_at = CURRENT_TIMESTAMP WHERE id = :id")
                .param("password", hashedPassword)
                .param("id", id)
                .update();
        if (rowsAffected != 1) {
            throw new DatabaseIntegrityException("Failed to update password");
        }
    }

    @Override
    public void updateLastLoginAtByLogin(String login) {
        int rowsAffected = jdbcClient.sql("UPDATE users SET last_login_at = CURRENT_TIMESTAMP WHERE login = :login")
                .param("login", login)
                .update();
        if (rowsAffected != 1) {
            throw new DatabaseIntegrityException("Failed to login");
        }
    }

    private static final String USER_WITH_ADDRESS_BASE_SELECT = """
            SELECT
                u.id               AS user_id,
                u.type             AS user_type,
                u.name             AS user_name,
                u.email            AS user_email,
                u.login            AS user_login,
                u.last_login_at    AS user_last_login_at,
                u.created_at       AS user_created_at,
                u.last_modified_at AS user_last_modified_at,
                a.id               AS address_id,
                a.zip_code         AS address_zip_code,
                a.country_code     AS address_country_code,
                a.state_code       AS address_state_code,
                a.city             AS address_city,
                a.street           AS address_street,
                a.number           AS address_number,
                a.complement       AS address_complement,
                a.created_at       AS address_created_at,
                a.last_modified_at AS address_last_modified_at
            FROM users u
            JOIN addresses a ON a.user_id = u.id
            """;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
        Address address = Address.builder()
                .id(rs.getLong("address_id"))
                .userId(rs.getLong("user_id"))
                .zipCode(rs.getString("address_zip_code"))
                .countryCode(rs.getString("address_country_code"))
                .stateCode(rs.getString("address_state_code"))
                .city(rs.getString("address_city"))
                .street(rs.getString("address_street"))
                .number(rs.getString("address_number"))
                .complement(rs.getString("address_complement"))
                .createdAt(rs.getObject("address_created_at", LocalDateTime.class))
                .lastModifiedAt(rs.getObject("address_last_modified_at", LocalDateTime.class))
                .build();

        return User.builder()
                .id(rs.getLong("user_id"))
                .type(UserType.valueOf(rs.getString("user_type")))
                .name(rs.getString("user_name"))
                .email(rs.getString("user_email"))
                .login(rs.getString("user_login"))
                .lastLoginAt(rs.getObject("user_last_login_at", LocalDateTime.class))
                .createdAt(rs.getObject("user_created_at", LocalDateTime.class))
                .lastModifiedAt(rs.getObject("user_last_modified_at", LocalDateTime.class))
                .address(address)
                .build();
    };
}
