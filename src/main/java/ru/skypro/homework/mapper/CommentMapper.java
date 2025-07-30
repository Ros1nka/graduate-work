package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

@Component
public class CommentMapper {

    public Comment toCommentDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        Comment dto = new Comment();

        dto.setPk(entity.getPk());
        dto.setText(entity.getText());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorImage("/image/users/" + entity.getAuthor().getId());
        dto.setCreatedAt((int) entity.getCreatedAt().toEpochMilli());
        return dto;
    }

    public CommentEntity toEntity(CreateOrUpdateComment dto, UserEntity author, AdEntity ad) {

        CommentEntity entity = new CommentEntity();

        entity.setText(dto.getText());
        entity.setAuthor(author);
        entity.setAd(ad);
        return entity;
    }

    public void updateEntityFromDto(CreateOrUpdateComment dto, CommentEntity entity) {
        entity.setText(dto.getText());
    }
}
