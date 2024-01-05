package com.cbt.EquipeAlcance.modules.news.repository;

import com.cbt.EquipeAlcance.modules.news.model.NewsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsModel, UUID> {
    Optional<NewsModel> findByIdPublic(UUID id);
    Optional<NewsModel> findByTitle(String title);

}
