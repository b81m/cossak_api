package ru.Lyalin.CossakText.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.entities.CossakImage;
import ru.Lyalin.CossakText.exceptions.ResourceNotFoundException;
import ru.Lyalin.CossakText.services.CossakImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cossak")
public class CossakImageController {

    private final CossakImageService cossakImageService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllCossakImages() {
        try{
            List<CossakImage>  cossakImages = cossakImageService.findAllCossakImages();
            return ResponseEntity.status(HttpStatus.OK).body(cossakImages);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCossakImageById(@PathVariable Long id) {
        try {
            CossakImage cossakImage = cossakImageService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(cossakImage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> createCossakImage(@RequestParam MultipartFile cossakImage) {
        try {
            CossakImageDto cossakImageDto = cossakImageService.saveCossakImage(cossakImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(cossakImageDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCossakImageById(@PathVariable Long id) {
        try {
            cossakImageService.deleteCossakImageById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
