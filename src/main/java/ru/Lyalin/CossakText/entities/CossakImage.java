package ru.Lyalin.CossakText.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(unique = true)
    private String originalImageName;

    @Column(unique = true)
    private String translatedImageName;

    private String fileType;
    private String text;
    private LocalDate creationDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CossakImage(String originalImageName, String translatedImageName, String fileType, String text, LocalDate creationDate) {
        this.originalImageName = originalImageName;
        this.translatedImageName = translatedImageName;
        this.fileType = fileType;
        this.text = text;
        this.creationDate = creationDate;
    }

}
