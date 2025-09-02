package ru.Lyalin.CossakText.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Lyalin.CossakText.entities.CossakImage;

@Repository
public interface CossakImageRepository extends JpaRepository<CossakImage,Long> {
}
