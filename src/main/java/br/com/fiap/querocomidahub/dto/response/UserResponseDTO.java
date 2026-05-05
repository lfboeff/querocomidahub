package br.com.fiap.querocomidahub.dto.response;

import br.com.fiap.querocomidahub.domain.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserResponseDTO(
        @Schema(description = "User ID", example = "1")
        Long id,

        @Schema(description = "User type", example = "CUSTOMER")
        UserType type,

        @Schema(description = "Full name", example = "Maria Silva")
        String name,

        @Schema(description = "Email address", example = "maria.silva@email.com")
        String email,

        @Schema(description = "Login username", example = "mariasilva")
        String login,

        @Schema(description = "Last login timestamp", example = "2026-04-25T10:00:00")
        LocalDateTime lastLoginAt,

        @Schema(description = "Creation timestamp", example = "2026-04-25T09:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification timestamp", example = "2026-04-25T09:30:00")
        LocalDateTime lastModifiedAt,

        @Schema(description = "User address")
        AddressResponseDTO address
) {
}
