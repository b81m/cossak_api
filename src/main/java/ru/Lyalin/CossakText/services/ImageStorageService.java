package ru.Lyalin.CossakText.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

@Service
public class ImageStorageService {
    private final Path storageDirectory;

    public ImageStorageService(@Value("${app.lyalin.image-storage-dir:uploads/cossak-images}") String storageDirectory) {
        this.storageDirectory = Path.of(storageDirectory).toAbsolutePath().normalize();
    }

    public String save(MultipartFile file) {
        try {
            return save(file.getBytes(), getExtension(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String save(byte[] image, MultipartFile sourceFile) {
        return save(image, getExtension(sourceFile.getOriginalFilename()));
    }

    public String save(byte[] image, String extension) {
        try {
            Files.createDirectories(storageDirectory);
            String imageName = UUID.randomUUID() + normalizeExtension(extension);
            Files.write(resolveImagePath(imageName), image);
            return imageName;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] read(String imageName) {
        try {
            return Files.readAllBytes(resolveImagePath(imageName));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(String imageName) {
        if (!StringUtils.hasText(imageName)) {
            return;
        }

        try {
            Files.deleteIfExists(resolveImagePath(imageName));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Path resolveImagePath(String imageName) {
        Path imagePath = storageDirectory.resolve(imageName).normalize();
        if (!imagePath.startsWith(storageDirectory)) {
            throw new IllegalArgumentException("Invalid image name");
        }
        return imagePath;
    }

    private String getExtension(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);
        return extension == null ? "" : extension;
    }

    private String normalizeExtension(String extension) {
        if (!StringUtils.hasText(extension)) {
            return "";
        }
        String normalizedExtension = extension.toLowerCase(Locale.ROOT);
        return normalizedExtension.startsWith(".") ? normalizedExtension : "." + normalizedExtension;
    }
}
