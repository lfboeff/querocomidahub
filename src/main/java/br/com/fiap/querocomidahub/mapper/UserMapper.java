package br.com.fiap.querocomidahub.mapper;

import br.com.fiap.querocomidahub.domain.Address;
import br.com.fiap.querocomidahub.domain.User;
import br.com.fiap.querocomidahub.dto.request.AddressRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserCreateRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserUpdateRequestDTO;
import br.com.fiap.querocomidahub.dto.response.AddressResponseDTO;
import br.com.fiap.querocomidahub.dto.response.UserResponseDTO;

public class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserCreateRequestDTO userCreateRequestDTO, String encryptedPassword) {
        return User.builder()
                .type(userCreateRequestDTO.type())
                .name(userCreateRequestDTO.name())
                .email(userCreateRequestDTO.email())
                .login(userCreateRequestDTO.login())
                .password(encryptedPassword)
                .build();
    }

    public static User toUser(UserUpdateRequestDTO userUpdateRequestDTO) {
        return User.builder()
                .type(userUpdateRequestDTO.type())
                .name(userUpdateRequestDTO.name())
                .email(userUpdateRequestDTO.email())
                .login(userUpdateRequestDTO.login())
                .build();
    }

    public static Address toAddress(Long userId, AddressRequestDTO addressRequestDTO) {
        return Address.builder()
                .userId(userId)
                .zipCode(addressRequestDTO.zipCode())
                .countryCode(addressRequestDTO.countryCode())
                .stateCode(addressRequestDTO.stateCode())
                .city(addressRequestDTO.city())
                .street(addressRequestDTO.street())
                .number(addressRequestDTO.number())
                .complement(addressRequestDTO.complement())
                .build();
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        AddressResponseDTO addressDTO = user.getAddress() != null
                ? toAddressResponseDTO(user.getAddress())
                : null;

        return new UserResponseDTO(
                user.getId(),
                user.getType(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getLastModifiedAt(),
                addressDTO
        );
    }

    private static AddressResponseDTO toAddressResponseDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getZipCode(),
                address.getCountryCode(),
                address.getStateCode(),
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getCreatedAt(),
                address.getLastModifiedAt()
        );
    }
}
