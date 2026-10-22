package ru.Lyalin.CossakText.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.Lyalin.CossakText.dto.CossakImageDetailsDto;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.dto.CossakUploadImageDto;
import ru.Lyalin.CossakText.services.CossakImageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cossak")
public class CossakImageController {

    private final CossakImageService cossakImageService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllCossakImages() {
        List<CossakImageDto> cossakImages = cossakImageService.findAllImages();
        return ResponseEntity.status(HttpStatus.OK).body(cossakImages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCossakImageById(@PathVariable Long id) {
        CossakImageDetailsDto cossakImage = cossakImageService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(cossakImage);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> createCossakImage(@RequestParam MultipartFile cossakImage) {
        CossakUploadImageDto cossakImageDto = cossakImageService.saveCossakImage(cossakImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(cossakImageDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCossakImageById(@PathVariable Long id) {
        cossakImageService.deleteCossakImageById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
    }
}
