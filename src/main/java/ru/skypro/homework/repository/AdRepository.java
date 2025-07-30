package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.model.AdEntity;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    List<AdEntity> findAllByAuthor(String name);

    List<AdEntity> findAllByAuthorId(int id);

    List<Comment> getCommentsByAdId(int adId);

    Optional<AdEntity> findById(int pk);
}
