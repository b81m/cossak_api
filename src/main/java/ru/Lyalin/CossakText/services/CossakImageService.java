package ru.Lyalin.CossakText.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.Lyalin.CossakText.dto.ApiResponseDto;
import ru.Lyalin.CossakText.dto.CossakImageDetailsDto;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.dto.CossakUploadImageDto;
import ru.Lyalin.CossakText.entities.CossakImage;
import ru.Lyalin.CossakText.entities.User;
import ru.Lyalin.CossakText.exceptions.BadRequestException;
import ru.Lyalin.CossakText.exceptions.ResourceNotFoundException;
import ru.Lyalin.CossakText.repositories.CossakImageRepository;
import ru.Lyalin.CossakText.repositories.UserRepository;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CossakImageService implements ICossakImageService {
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    private final CossakImageRepository cossakImageRepository;
    private final CertificateTranslatorService certificateTranslatorService;
    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

    @Override
    public List<CossakImageDto> findAllImages() {
        User currentUser = getCurrentUser();
        List<CossakImage> cossakImages = cossakImageRepository.findByUser_Id(currentUser.getId());
        return cossakImages.stream()
                .map(this::toDtoWithImages)
                .toList();
    }

    private CossakImageDto toDtoWithImages(CossakImage cossakImage) {
        return new CossakImageDto(
                cossakImage.getId(),
                imageStorageService.read(cossakImage.getOriginalImageName()),
                cossakImage.getOriginalImageName(),
                imageStorageService.read(cossakImage.getTranslatedImageName()),
                cossakImage.getTranslatedImageName(),
                cossakImage.getFileType(),
                cossakImage.getText(),
                cossakImage.getCreationDate()
        );
    }

    @Override
    public CossakImageDetailsDto findById(Long id) {
        User currentUser = getCurrentUser();
        Optional<CossakImage> cossakImage = cossakImageRepository.findCossakByIdAndUserId(id, currentUser.getId());
        return cossakImage
                .map(this::toDetailsDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cossak image not founded"));
    }

    private CossakImageDetailsDto toDetailsDto(CossakImage cossakImage) {
        return new CossakImageDetailsDto(
                cossakImage.getId(),
                imageStorageService.read(cossakImage.getOriginalImageName()),
                imageStorageService.read(cossakImage.getTranslatedImageName()),
                cossakImage.getFileType(),
                cossakImage.getText()
        );
    }

    @Override
    @Transactional
    public void deleteCossakImageById(Long id) {
        User currentUser = getCurrentUser();
        cossakImageRepository.findCossakByIdAndUserId(id, currentUser.getId()).ifPresentOrElse(cossakImage -> {
            String originalImageName = cossakImage.getOriginalImageName();
            String translatedImageName = cossakImage.getTranslatedImageName();
            cossakImageRepository.delete(cossakImage);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    imageStorageService.delete(originalImageName);
                    imageStorageService.delete(translatedImageName);
                }
            });
        }, () -> {
            throw new ResourceNotFoundException("CossakImage not found" + id);
        });
    }

    @Override
    @Transactional
    public CossakUploadImageDto saveCossakImage(MultipartFile file) {
        User currentUser = getCurrentUser();
        String originalImageName = null;
        String translatedImageName = null;
        try {
            validateUploadedImage(file);
            byte[] originalFileBytes = file.getBytes();
            validateImageBytes(originalFileBytes, "Uploaded file is not a valid image");
            String fileType = file.getContentType();

            ApiResponseDto apiResponseDto = certificateTranslatorService.getTranscript(originalFileBytes);
            validateImageBytes(apiResponseDto.transcribedImage(), "Translated file is not a valid image");
            originalImageName = imageStorageService.save(originalFileBytes, file);
            translatedImageName = imageStorageService.save(apiResponseDto.transcribedImage(), file);

            CossakImage cossakImage = new CossakImage(originalImageName,
                    translatedImageName,
                    fileType,
                    apiResponseDto.transcript(),
                    LocalDate.now());
            cossakImage.setUser(currentUser);

            cossakImageRepository.save(cossakImage);

            return new CossakUploadImageDto(apiResponseDto.transcribedImage(),translatedImageName, fileType, apiResponseDto.transcript());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (RuntimeException e) {
            deleteSavedImage(originalImageName);
            deleteSavedImage(translatedImageName);
            throw e;
        }
    }

    private void deleteSavedImage(String imageName) {
        try {
            imageStorageService.delete(imageName);
        } catch (RuntimeException ignored) {
        }
    }

    private void validateUploadedImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Only JPEG and PNG images are supported");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException("Image file must have jpg, jpeg or png extension");
        }
    }

    private void validateImageBytes(byte[] image, String message) {
        if (image == null || image.length == 0) {
            throw new BadRequestException(message);
        }

        try {
            if (ImageIO.read(new ByteArrayInputStream(image)) == null) {
                throw new BadRequestException(message);
            }
        } catch (IOException e) {
            throw new BadRequestException(message);
        }
    }
}
