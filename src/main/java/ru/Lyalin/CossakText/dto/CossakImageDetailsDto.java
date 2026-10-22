package ru.Lyalin.CossakText.dto;

public record CossakImageDetailsDto(Long id, byte[] originalImage, byte[] translatedImage, String fileType, String text) {
}
