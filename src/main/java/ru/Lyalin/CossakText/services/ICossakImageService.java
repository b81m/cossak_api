package ru.Lyalin.CossakText.services;

import org.springframework.web.multipart.MultipartFile;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.entities.CossakImage;

import java.util.List;

public interface ICossakImageService {
    CossakImage findById(Long id);
    void deleteCossakImageById(Long id);
    CossakImageDto saveCossakImage(MultipartFile file);
    List<CossakImage> findAllImages();
}
