package br.com.fiap.querocomidahub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @Schema(description = "Login username", example = "mariasilva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 3, max = 100)
        String login,

        @Schema(description = "Password", example = "senha@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 8, max = 255)
        String password
) {
    @Override
    public String toString() {
        return "LoginRequestDTO[" +
                "login=" + login +
                ", password=" + "**REDACTED**" +
                "]";
    }
}
