package br.com.fiap.querocomidahub.repository;

import br.com.fiap.querocomidahub.domain.Address;
import br.com.fiap.querocomidahub.exception.DatabaseIntegrityException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final JdbcClient jdbcClient;

    public AddressRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Long save(Address address) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                        INSERT INTO addresses (user_id, zip_code, country_code, state_code, city, street, number, complement)
                        VALUES (:userId, :zipCode, :countryCode, :stateCode, :city, :street, :number, :complement)
                        """)
                .param("userId", address.getUserId())
                .param("zipCode", address.getZipCode())
                .param("countryCode", address.getCountryCode())
                .param("stateCode", address.getStateCode())
                .param("city", address.getCity())
                .param("street", address.getStreet())
                .param("number", address.getNumber())
                .param("complement", address.getComplement())
                .update(keyHolder);
        var key = keyHolder.getKey();
        if (key == null) {
            throw new DatabaseIntegrityException("Failed to retrieve generated key after saving address");
        }
        return key.longValue();
    }

    @Override
    public void update(Long userId, Address address) {
        int rowsAffected = jdbcClient.sql("""
                        UPDATE addresses
                        SET zip_code = :zipCode, country_code = :countryCode, state_code = :stateCode,
                            city = :city, street = :street, number = :number, complement = :complement,
                            last_modified_at = CURRENT_TIMESTAMP
                        WHERE user_id = :userId
                        """)
                .param("userId", userId)
                .param("zipCode", address.getZipCode())
                .param("countryCode", address.getCountryCode())
                .param("stateCode", address.getStateCode())
                .param("city", address.getCity())
                .param("street", address.getStreet())
                .param("number", address.getNumber())
                .param("complement", address.getComplement())
                .update();
        if (rowsAffected != 1) {
            throw new DatabaseIntegrityException("Failed to update address");
        }
    }
}
