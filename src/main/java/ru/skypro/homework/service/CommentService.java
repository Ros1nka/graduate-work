package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;

public interface CommentService {

    Comments getComments(int AdId);

    Comment addComment(int id, CreateOrUpdateComment updateComment, String username);

    void deleteComment(int adId, int commentId, String username)
            throws AdNotFoundException, ForbiddenException;

    Comment updateComment(int adId, int commentId, CreateOrUpdateComment updatedComment, String username)
            throws AdNotFoundException, ForbiddenException;
}
