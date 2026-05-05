package br.com.fiap.querocomidahub.repository;

import br.com.fiap.querocomidahub.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    List<User> findByName(String name);

    Optional<User> findByLogin(String login);

    Long save(User user);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    boolean existsByEmailForDifferentUser(String email, Long id);

    boolean existsByLoginForDifferentUser(String login, Long id);

    void update(Long id, User user);

    void delete(Long id);

    String findPasswordById(Long id);

    Optional<String> findPasswordByLogin(String login);

    void updatePasswordById(Long id, String hashedPassword);

    void updateLastLoginAtByLogin(String login);
}
