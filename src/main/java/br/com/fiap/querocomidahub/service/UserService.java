package br.com.fiap.querocomidahub.service;

import br.com.fiap.querocomidahub.dto.request.AddressRequestDTO;
import br.com.fiap.querocomidahub.dto.request.ChangePasswordRequestDTO;
import br.com.fiap.querocomidahub.dto.request.LoginRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserCreateRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserUpdateRequestDTO;
import br.com.fiap.querocomidahub.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getUsers(String name);

    Long createUser(UserCreateRequestDTO request);

    void updateUser(Long id, UserUpdateRequestDTO request);

    void deleteUser(Long id);

    void updateAddress(Long userId, AddressRequestDTO request);

    void changePassword(Long id, ChangePasswordRequestDTO request);

    UserResponseDTO login(LoginRequestDTO request);
}
