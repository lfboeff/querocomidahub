package br.com.fiap.querocomidahub.service;

import br.com.fiap.querocomidahub.domain.Address;
import br.com.fiap.querocomidahub.domain.User;
import br.com.fiap.querocomidahub.dto.request.AddressRequestDTO;
import br.com.fiap.querocomidahub.dto.request.ChangePasswordRequestDTO;
import br.com.fiap.querocomidahub.dto.request.LoginRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserCreateRequestDTO;
import br.com.fiap.querocomidahub.dto.request.UserUpdateRequestDTO;
import br.com.fiap.querocomidahub.dto.response.UserResponseDTO;
import br.com.fiap.querocomidahub.exception.DatabaseIntegrityException;
import br.com.fiap.querocomidahub.exception.UserDuplicateCredentialsException;
import br.com.fiap.querocomidahub.exception.UserInvalidPasswordException;
import br.com.fiap.querocomidahub.exception.UserLoginFailedException;
import br.com.fiap.querocomidahub.exception.UserNotFoundException;
import br.com.fiap.querocomidahub.mapper.UserMapper;
import br.com.fiap.querocomidahub.repository.AddressRepository;
import br.com.fiap.querocomidahub.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDTO> getUsers(String name) {
        List<User> users = (name != null && !name.isBlank())
                ? userRepository.findByName(name)
                : userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserResponseDTO)
                .toList();
    }

    @Transactional
    @Override
    public Long createUser(UserCreateRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserDuplicateCredentialsException("Email already in use");
        }
        if (userRepository.existsByLogin(request.login())) {
            throw new UserDuplicateCredentialsException("Login already in use");
        }

        String encryptedPassword = passwordEncoder.encode(request.password());
        User user = UserMapper.toUser(request, encryptedPassword);
        Long userId = userRepository.save(user);

        Address address = UserMapper.toAddress(userId, request.address());
        addressRepository.save(address);
        return userId;
    }

    @Override
    public void updateUser(Long id, UserUpdateRequestDTO request) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        if (userRepository.existsByEmailForDifferentUser(request.email(), id)) {
            throw new UserDuplicateCredentialsException("Email already in use");
        }
        if (userRepository.existsByLoginForDifferentUser(request.login(), id)) {
            throw new UserDuplicateCredentialsException("Login already in use");
        }

        User user = UserMapper.toUser(request);
        userRepository.update(id, user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.delete(id);
    }

    @Override
    public void updateAddress(Long userId, AddressRequestDTO request) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Address address = UserMapper.toAddress(userId, request);
        addressRepository.update(userId, address);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequestDTO request) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        String storedHash = userRepository.findPasswordById(id);
        if (!passwordEncoder.matches(request.currentPassword(), storedHash)) {
            throw new UserInvalidPasswordException();
        }

        userRepository.updatePasswordById(id, passwordEncoder.encode(request.newPassword()));
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO request) {
        String storedHash = userRepository.findPasswordByLogin(request.login())
                .orElseThrow(UserLoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), storedHash)) {
            throw new UserLoginFailedException();
        }

        userRepository.updateLastLoginAtByLogin(request.login());

        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new DatabaseIntegrityException("User not found after successful authentication for login: " + request.login()));

        return UserMapper.toUserResponseDTO(user);
    }
}
