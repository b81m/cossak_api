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
import ru.Lyalin.CossakText.entities.User;
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
    public User register(@RequestBody RegistrationRequestDto registrationInfo) {
        return userService.saveUser(registrationInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody RegistrationRequestDto registrationInfo) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registrationInfo.username(), registrationInfo.password()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.OK).body(new JwtDto(jwtService.generateToken(registrationInfo.username())));
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    @GetMapping("/user/name")
    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
