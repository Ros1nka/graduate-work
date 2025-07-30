package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.model.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByAdId(int adId);

    Optional<CommentEntity> findByIdAndAdId(int commentId, int adId);

    void deleteByPkAndAuthorId(int commentId, int authorId);
}
