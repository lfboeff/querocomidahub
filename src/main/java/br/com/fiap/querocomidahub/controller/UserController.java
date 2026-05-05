package br.com.fiap.querocomidahub.controller;

import br.com.fiap.querocomidahub.dto.request.AddressRequestDTO;
import br.com.fiap.querocomidahub.dto.request.ChangePasswordRequestDTO;
import br.com.fiap.querocomidahub.dto.request.LoginRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserCreateRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserUpdateRequestDTO;
import br.com.fiap.querocomidahub.dto.response.UserResponseDTO;
import br.com.fiap.querocomidahub.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam(required = false) String name) {
        logRequestReceived(HttpMethod.GET);
        List<UserResponseDTO> users = userService.getUsers(name);
        LOGGER.info("Total users found: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserCreateRequestDTO request) {
        logRequestReceived(HttpMethod.POST, request);
        Long userId = userService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userId).toUri();
        LOGGER.info("Created user with id={}", userId);
        return ResponseEntity.created(location).build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateRequestDTO request) {
        logRequestReceived(HttpMethod.PUT, request);
        userService.updateUser(id, request);
        LOGGER.info("Updated user with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logRequestReceived(HttpMethod.DELETE);
        userService.deleteUser(id);
        LOGGER.info("User with id={} deleted", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}/address")
    public ResponseEntity<Void> updateAddress(@PathVariable Long id, @RequestBody @Valid AddressRequestDTO request) {
        logRequestReceived(HttpMethod.PUT, request);
        userService.updateAddress(id, request);
        LOGGER.info("Updated address for user with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequestDTO request) {
        logRequestReceived(HttpMethod.PATCH);
        userService.changePassword(id, request);
        LOGGER.info("Updated password for user with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        logRequestReceived(HttpMethod.POST, request);
        UserResponseDTO user = userService.login(request);
        LOGGER.info("Login successful for user with login='{}'", request.login());
        return ResponseEntity.ok(user);
    }

    private void logRequestReceived(HttpMethod httpMethod) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        LOGGER.info("{} request received at '{}'", httpMethod, uri);
    }

    private void logRequestReceived(HttpMethod httpMethod, Object body) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        LOGGER.info("{} request received at '{}' with body={}", httpMethod, uri, body);
    }
}
