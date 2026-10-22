package ru.Lyalin.CossakText.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Lyalin.CossakText.dto.RegistrationRequestDto;
import ru.Lyalin.CossakText.dto.UserResponseDto;
import ru.Lyalin.CossakText.entities.User;
import ru.Lyalin.CossakText.enums.Role;
import ru.Lyalin.CossakText.exceptions.BadRequestException;
import ru.Lyalin.CossakText.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto saveUser(RegistrationRequestDto registrationInfo) {
        validateRegistrationInfo(registrationInfo);
        String username = registrationInfo.username().trim();

        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username is already taken");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(registrationInfo.password()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }

    private void validateRegistrationInfo(RegistrationRequestDto registrationInfo) {
        if (registrationInfo == null || registrationInfo.username() == null || registrationInfo.username().isBlank()) {
            throw new BadRequestException("Username is required");
        }
        if (registrationInfo.password() == null || registrationInfo.password().isBlank()) {
            throw new BadRequestException("Password is required");
        }
    }
}
