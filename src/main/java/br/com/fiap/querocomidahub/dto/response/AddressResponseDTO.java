package br.com.fiap.querocomidahub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record AddressResponseDTO(
        @Schema(description = "Address ID", example = "1")
        Long id,

        @Schema(description = "ZIP / postal code", example = "90000-000")
        String zipCode,

        @Schema(description = "ISO 3166-1 alpha-2 country code", example = "BR")
        String countryCode,

        @Schema(description = "State or province code", example = "RS")
        String stateCode,

        @Schema(description = "City name", example = "Porto Alegre")
        String city,

        @Schema(description = "Street name", example = "Av Brasil")
        String street,

        @Schema(description = "Street number", example = "123")
        String number,

        @Schema(description = "Complement", example = "Apto 23")
        String complement,

        @Schema(description = "Creation timestamp", example = "2026-04-25T09:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification timestamp", example = "2026-04-25T10:15:00")
        LocalDateTime lastModifiedAt
) {
}
