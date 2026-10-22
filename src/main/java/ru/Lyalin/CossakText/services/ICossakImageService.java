package ru.Lyalin.CossakText.services;

import org.springframework.web.multipart.MultipartFile;
import ru.Lyalin.CossakText.dto.CossakImageDetailsDto;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.dto.CossakUploadImageDto;

import java.util.List;

public interface ICossakImageService {
    CossakImageDetailsDto findById(Long id);

    void deleteCossakImageById(Long id);

    CossakUploadImageDto saveCossakImage(MultipartFile file);

    List<CossakImageDto> findAllImages();
}
