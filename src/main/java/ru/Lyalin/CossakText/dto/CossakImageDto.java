package ru.Lyalin.CossakText.dto;

import java.time.LocalDate;

public record CossakImageDto(Long id, byte[] originalImage, String originalImageName, byte[] translatedImage,
                             String translatedImageName, String fileType, String text,
                             LocalDate creationDate) {
}
