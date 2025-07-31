package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByAdPk(int adId);

    Optional<CommentEntity> findByPkAndAdPk(int commentId, int adId);

    Optional<CommentEntity> findByPk(int commentId);
}
