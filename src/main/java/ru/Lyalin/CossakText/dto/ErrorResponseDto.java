package ru.Lyalin.CossakText.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(String message, int status, LocalDateTime timestamp) {
}
