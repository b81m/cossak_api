package ru.Lyalin.CossakText.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.Lyalin.CossakText.dto.ApiResponseDto;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.entities.CossakImage;
import ru.Lyalin.CossakText.exceptions.ResourceNotFoundException;
import ru.Lyalin.CossakText.repositories.CossakImageRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CossakImageService implements ICossakImageService {

    private final CossakImageRepository cossakImageRepository;
    private final CertificateTranslatorService certificateTranslatorService;

    @Override
    public CossakImage findById(Long id) {
        return cossakImageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CossakImage not found"));
    }

    @Override
    public void deleteCossakImageById(Long id) {
        cossakImageRepository.findById(id).ifPresentOrElse(cossakImageRepository::delete, () -> {
            throw new ResourceNotFoundException("CossakImage not found" + id);
        });
    }

    @Override
    @Transactional
    public CossakImageDto saveCossakImage(MultipartFile file) {

        try {
            byte[] originalFileBytes = file.getBytes();
            String fileType = file.getContentType();

            ApiResponseDto apiResponseDto = certificateTranslatorService.getTranslation(originalFileBytes);
            String translatedImageName = UUID.randomUUID().toString();

            CossakImage cossakImage = new CossakImage(originalFileBytes,
                    UUID.randomUUID().toString(),
                    apiResponseDto.translatedImage(),
                    translatedImageName,
                    fileType,
                    apiResponseDto.translation(),
                    LocalDate.now());

            cossakImageRepository.save(cossakImage);

            return new CossakImageDto(apiResponseDto.translatedImage(), translatedImageName, fileType, apiResponseDto.translation());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CossakImage> findAllCossakImages() {
        List<CossakImage> cossakImages = cossakImageRepository.findAll();
        if (cossakImages.isEmpty()) {
            throw new ResourceNotFoundException("Cossak images not founded");
        }
        return cossakImages;
    }
}
