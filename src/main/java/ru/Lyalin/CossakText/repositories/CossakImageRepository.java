package ru.Lyalin.CossakText.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.Lyalin.CossakText.dto.CossakImageDto;
import ru.Lyalin.CossakText.entities.CossakImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface CossakImageRepository extends JpaRepository<CossakImage,Long> {
    @Query("SELECT new ru.Lyalin.CossakText.dto.CossakImageDto(c.id, c.originalImage,c.originalImageName, c.translatedImage,c.translatedImageName, c.fileType, c.text, c.creationDate) FROM CossakImage c WHERE c.user.id = :id")
    List<CossakImageDto> findByUser_Id(Long id);
    @Query("SELECT c FROM CossakImage c WHERE c.id = :imageId and c.user.id = :userId")
    Optional<CossakImage> findCossakByIdAndUserId(@Param("imageId") Long imageId,@Param("userId")  Long userId);
}
