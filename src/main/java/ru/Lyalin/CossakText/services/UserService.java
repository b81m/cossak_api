package ru.Lyalin.CossakText.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Lyalin.CossakText.dto.RegistrationRequestDto;
import ru.Lyalin.CossakText.entities.User;
import ru.Lyalin.CossakText.enums.Role;
import ru.Lyalin.CossakText.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User saveUser(RegistrationRequestDto registrationInfo) {
        User user = new User();
        user.setUsername(registrationInfo.username());
        user.setPassword(bCryptPasswordEncoder.encode(registrationInfo.password()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
}
