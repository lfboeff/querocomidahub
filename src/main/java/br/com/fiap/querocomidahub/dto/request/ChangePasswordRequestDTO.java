package br.com.fiap.querocomidahub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
        @Schema(description = "Current Password", example = "senha@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 8, max = 255)
        String currentPassword,

        @Schema(description = "New Password", example = "novasenha@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 8, max = 255)
        String newPassword
) {
    @Override
    public String toString() {
        return "ChangePasswordRequestDTO[" +
                "currentPassword=" + "**REDACTED**" +
                ", newPassword=" + "**REDACTED**" +
                "]";
    }
}
