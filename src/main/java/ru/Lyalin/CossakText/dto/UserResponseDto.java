package ru.Lyalin.CossakText.dto;

import ru.Lyalin.CossakText.enums.Role;

public record UserResponseDto(Long id, String username, Role role) {
}
