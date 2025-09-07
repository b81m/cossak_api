package ru.Lyalin.CossakText.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.Lyalin.CossakText.dto.ApiResponseDto;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.dto.CossakUploadImageDto;
import ru.Lyalin.CossakText.entities.CossakImage;
import ru.Lyalin.CossakText.entities.User;
import ru.Lyalin.CossakText.exceptions.ResourceNotFoundException;
import ru.Lyalin.CossakText.repositories.CossakImageRepository;
import ru.Lyalin.CossakText.repositories.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CossakImageService implements ICossakImageService {

    private final CossakImageRepository cossakImageRepository;
    private final CertificateTranslatorService certificateTranslatorService;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

    @Override
    public List<CossakImageDto> findAllImages() {
        User currentUser = getCurrentUser();
        List<CossakImageDto> cossakImages = cossakImageRepository.findByUser_Id(currentUser.getId());
        if (cossakImages.isEmpty()) {
            throw new ResourceNotFoundException("Cossak images not founded");
        }
        return cossakImages;
    }

    @Override
    public CossakImage findById(Long id) {
        User currentUser = getCurrentUser();
        Optional<CossakImage> cossakImage = cossakImageRepository.findCossakByIdAndUserId(id, currentUser.getId());
        return cossakImage.orElseThrow(() -> new ResourceNotFoundException("Cossak image not founded"));
    }

    @Override
    public void deleteCossakImageById(Long id) {
        cossakImageRepository.findById(id).ifPresentOrElse(cossakImageRepository::delete, () -> {
            throw new ResourceNotFoundException("CossakImage not found" + id);
        });
    }

    @Override
    @Transactional
    public CossakUploadImageDto saveCossakImage(MultipartFile file) {
        User currentUser = getCurrentUser();
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
            cossakImage.setUser(currentUser);

            cossakImageRepository.save(cossakImage);

            return new CossakUploadImageDto(apiResponseDto.translatedImage(), translatedImageName, fileType, apiResponseDto.translation());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
