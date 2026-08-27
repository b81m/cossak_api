package ru.Lyalin.CossakText.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.Lyalin.CossakText.dto.JwtDto;
import ru.Lyalin.CossakText.dto.RegistrationRequestDto;
import ru.Lyalin.CossakText.dto.UserResponseDto;
import ru.Lyalin.CossakText.exceptions.BadRequestException;
import ru.Lyalin.CossakText.services.JwtService;
import ru.Lyalin.CossakText.services.UserService;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody RegistrationRequestDto registrationInfo) {
        return userService.saveUser(registrationInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody RegistrationRequestDto registrationInfo) {
        validateLoginInfo(registrationInfo);
        String username = registrationInfo.username().trim();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, registrationInfo.password()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.OK).body(new JwtDto(jwtService.generateToken(username)));
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/username")
    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void validateLoginInfo(RegistrationRequestDto registrationInfo) {
        if (registrationInfo == null || registrationInfo.username() == null || registrationInfo.username().isBlank()) {
            throw new BadRequestException("Username is required");
        }
        if (registrationInfo.password() == null || registrationInfo.password().isBlank()) {
            throw new BadRequestException("Password is required");
        }
    }

}
