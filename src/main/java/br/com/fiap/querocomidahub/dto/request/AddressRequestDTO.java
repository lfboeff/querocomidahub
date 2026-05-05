package br.com.fiap.querocomidahub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @Schema(description = "ZIP / postal code", example = "90000-000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 20)
        String zipCode,

        @Schema(description = "ISO 3166-1 alpha-2 country code", example = "BR", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 2, max = 2, message = "must be exactly 2 characters following ISO 3166-1 alpha-2")
        String countryCode,

        @Schema(description = "State or province code", example = "RS", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 2, max = 10)
        String stateCode,

        @Schema(description = "City name", example = "Porto Alegre", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 100)
        String city,

        @Schema(description = "Street name", example = "Av Brasil", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 255)
        String street,

        @Schema(description = "Street number", example = "123")
        @Size(max = 20)
        String number,

        @Schema(description = "Complement", example = "Apto 23")
        @Size(max = 255)
        String complement
) {
}
