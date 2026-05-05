package br.com.fiap.querocomidahub.controller;

import br.com.fiap.querocomidahub.dto.request.AddressRequestDTO;
import br.com.fiap.querocomidahub.dto.request.ChangePasswordRequestDTO;
import br.com.fiap.querocomidahub.dto.request.LoginRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserCreateRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserUpdateRequestDTO;
import br.com.fiap.querocomidahub.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Users", description = "User management endpoints")
public interface UserApi {

    // -------------------------------------------------------------------------
    // GET /api/v1/users
    // -------------------------------------------------------------------------
    @Operation(
            summary = "List users",
            description = """
                    Returns a list of all registered users.
                    If the 'name' query parameter is provided, returns only users whose name partially matches it (case-insensitive).
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<List<UserResponseDTO>> getUsers(
            @Parameter(name = "name", description = "If not blank, will filter users by name (partial match, case-insensitive).", example = "Maria")
            @RequestParam(required = false) String name
    );

    // -------------------------------------------------------------------------
    // POST /api/v1/users
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Create user",
            description = "Creates a new user along with their address. Email and login must be unique."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            headers = @Header(name = "Location", description = "URL of the created user", schema = @Schema(implementation = String.class)),
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Validation Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "Request validation failed",
                                      "instance": "/api/v1/users",
                                      "errors": [
                                        "type: must not be null",
                                        "name: must not be blank",
                                        "email: must be a well-formed email address",
                                        "login: size must be between 3 and 100",
                                        "address.zipCode: must not be blank"
                                      ]
                                    }
                                    """),
                            @ExampleObject(name = "Malformed Request", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "The request body is malformed or missing",
                                      "instance": "/api/v1/users"
                                    }
                                    """)
                    }
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Email or login already in use",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Conflict",
                              "status": 409,
                              "detail": "Email already in use",
                              "instance": "/api/v1/users"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users"
                                    }
                                    """),
                            @ExampleObject(name = "Database Integrity Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "Failed to retrieve generated key after saving user",
                                      "instance": "/api/v1/users"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<Void> createUser(
            @RequestBody(description = "User data to create. Attention to 'type', which is an enum.", required = true) UserCreateRequestDTO request
    );

    // -------------------------------------------------------------------------
    // PUT /api/v1/users/{id}
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Update user",
            description = "Updates an existing user's information. Email and login must remain unique."
    )
    @ApiResponse(
            responseCode = "204",
            description = "User updated successfully",
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Validation Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "Request validation failed",
                                      "instance": "/api/v1/users/1",
                                      "errors": [
                                        "type: must not be null",
                                        "email: must be a well-formed email address",
                                        "login: size must be between 3 and 100",
                                        "name: must not be blank"
                                      ]
                                    }
                                    """),
                            @ExampleObject(name = "Malformed Request", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "The request body is malformed or missing",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """)
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Not Found",
                              "status": 404,
                              "detail": "User with id=9999 was not found",
                              "instance": "/api/v1/users/9999"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Email or login already in use",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Conflict",
                              "status": 409,
                              "detail": "Email already in use",
                              "instance": "/api/v1/users/1"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """),
                            @ExampleObject(name = "Database Integrity Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "Failed to update user",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<Void> updateUser(
            @Parameter(name = "id", description = "ID of the user to update.", example = "1", required = true) @PathVariable Long id,
            @RequestBody(description = "Updated user data", required = true) UserUpdateRequestDTO request
    );

    // -------------------------------------------------------------------------
    // DELETE /api/v1/users/{id}
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Delete user",
            description = "Permanently deletes a user and their associated address."
    )
    @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully",
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Not Found",
                              "status": 404,
                              "detail": "User with id=9999 was not found",
                              "instance": "/api/v1/users/9999"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """),
                            @ExampleObject(name = "Database Integrity Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "Failed to delete user",
                                      "instance": "/api/v1/users/1"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<Void> deleteUser(
            @Parameter(name = "id", description = "ID of the user to delete", example = "1", required = true) @PathVariable Long id
    );

    // -------------------------------------------------------------------------
    // PUT /api/v1/users/{id}/address
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Update address",
            description = "Replaces the address of an existing user."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Address updated successfully",
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Validation Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "Request validation failed",
                                      "instance": "/api/v1/users/1/address",
                                      "errors": [
                                        "zipCode: must not be blank",
                                        "countryCode: must be exactly 2 characters following ISO 3166-1 alpha-2"
                                      ]
                                    }
                                    """),
                            @ExampleObject(name = "Malformed Request", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "The request body is malformed or missing",
                                      "instance": "/api/v1/users/1/address"
                                    }
                                    """)
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Not Found",
                              "status": 404,
                              "detail": "User with id=9999 was not found",
                              "instance": "/api/v1/users/9999/address"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users/1/address"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users/1/address"
                                    }
                                    """),
                            @ExampleObject(name = "Database Integrity Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "Failed to update address",
                                      "instance": "/api/v1/users/1/address"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<Void> updateAddress(
            @Parameter(name = "id", description = "ID of the user whose address to update", example = "1", required = true) @PathVariable Long id,
            @RequestBody(description = "New address data", required = true) AddressRequestDTO request
    );

    // -------------------------------------------------------------------------
    // PATCH /api/v1/users/{id}/password
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Change password",
            description = "Changes the password of an existing user. Requires the current password for verification."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Password changed successfully",
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Validation Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "Request validation failed",
                                      "instance": "/api/v1/users/1/password",
                                      "errors": [
                                        "newPassword: size must be between 8 and 255"
                                      ]
                                    }
                                    """),
                            @ExampleObject(name = "Malformed Request", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "The request body is malformed or missing",
                                      "instance": "/api/v1/users/1/password"
                                    }
                                    """)
                    }
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Current password is incorrect",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Unauthorized",
                              "status": 401,
                              "detail": "Current password is incorrect",
                              "instance": "/api/v1/users/1/password"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Not Found",
                              "status": 404,
                              "detail": "User with id=9999 was not found",
                              "instance": "/api/v1/users/9999/password"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users/1/password"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users/1/password"
                                    }
                                    """),
                            @ExampleObject(name = "Database Integrity Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "Failed to update password",
                                      "instance": "/api/v1/users/1/password"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<Void> changePassword(
            @Parameter(name = "id", description = "ID of the user whose password to change", example = "1", required = true) @PathVariable Long id,
            @RequestBody(description = "Current and new password", required = true) ChangePasswordRequestDTO request
    );

    // -------------------------------------------------------------------------
    // POST /api/v1/users/login
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Login",
            description = "Validates the user's credentials and returns their profile on success."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Validation Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "Request validation failed",
                                      "instance": "/api/v1/users/login",
                                      "errors": [
                                        "login: must not be blank",
                                        "login: size must be between 3 and 100"
                                      ]
                                    }
                                    """),
                            @ExampleObject(name = "Malformed Request", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "The request body is malformed or missing",
                                      "instance": "/api/v1/users/login"
                                    }
                                    """)
                    }
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Unauthorized",
                              "status": 401,
                              "detail": "Invalid login or password",
                              "instance": "/api/v1/users/login"
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "Database Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "A database error occurred",
                                      "instance": "/api/v1/users/login"
                                    }
                                    """),
                            @ExampleObject(name = "Internal Server Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "An unexpected error occurred",
                                      "instance": "/api/v1/users/login"
                                    }
                                    """),
                            @ExampleObject(name = "Database Integrity Error", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Internal Server Error",
                                      "status": 500,
                                      "detail": "Failed to login",
                                      "instance": "/api/v1/users/login"
                                    }
                                    """)
                    }
            )
    )
    ResponseEntity<UserResponseDTO> login(
            @RequestBody(description = "Login credentials", required = true) LoginRequestDTO request
    );
}
