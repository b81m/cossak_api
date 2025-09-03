package ru.Lyalin.CossakText.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cossak_images")
public class CossakImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] originalImage;
    private String originalImageName;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] translatedImage;
    private String translatedImageName;

    private String fileType;
    private String text;
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CossakImage(byte[] originalImage, String originalImageName, byte[] translatedImage, String translatedImageName, String fileType, String text, LocalDate creationDate) {
        this.originalImage = originalImage;
        this.originalImageName = originalImageName;
        this.translatedImage = translatedImage;
        this.translatedImageName = translatedImageName;
        this.fileType = fileType;
        this.text = text;
        this.creationDate = creationDate;
    }

}
