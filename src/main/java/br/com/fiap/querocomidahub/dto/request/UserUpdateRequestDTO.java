package br.com.fiap.querocomidahub.dto.request;

import br.com.fiap.querocomidahub.domain.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @Schema(description = "User type", example = "CUSTOMER", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        UserType type,

        @Schema(description = "Full name", example = "Maria Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 150)
        String name,

        @Schema(description = "Email address", example = "maria.silva@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email @Size(max = 255)
        String email,

        @Schema(description = "Login username", example = "mariasilva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 3, max = 100)
        String login
) {
}
