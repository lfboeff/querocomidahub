package br.com.fiap.querocomidahub.repository;

import br.com.fiap.querocomidahub.domain.Address;

public interface AddressRepository {
    Long save(Address address);

    void update(Long userId, Address address);
}
