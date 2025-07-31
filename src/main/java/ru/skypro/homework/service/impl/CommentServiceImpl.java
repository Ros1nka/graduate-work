package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(AdRepository adRepository,
                              CommentMapper commentMapper,
                              CommentRepository commentRepository,
                              UserRepository userRepository) {
        this.adRepository = adRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Comments getComments(int adId) {

        List<CommentEntity> comments = commentRepository.findByAdPk(adId);

        List<Comment> result = comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

        return new Comments(result.size(), result);
    }

    @Override
    public Comment addComment(int adId, CreateOrUpdateComment updateComment, String username) {

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CommentEntity commentEntity = commentMapper.toEntity(updateComment, user, ad);

        CommentEntity savedComment = commentRepository.save(commentEntity);

        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public void deleteComment(int adId, int commentId, String username) throws AdNotFoundException, ForbiddenException {

        CommentEntity comment = commentRepository.findByPkAndAdPk(commentId, adId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        commentRepository.delete(comment);
    }

    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updatedComment, String username)
            throws AdNotFoundException, ForbiddenException {

        CommentEntity comment = commentRepository.findByPkAndAdPk(commentId, adId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        comment.setText(updatedComment.getText());

        CommentEntity updatedCommentEntity = commentRepository.save(comment);

        return commentMapper.toCommentDto(updatedCommentEntity);
    }

    public boolean isCommentAuthorOrAdmin(int commentId, String username) {
        CommentEntity comment = commentRepository.findByPk(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return comment.getAuthor().getId() == user.getId() || user.getRole() == Role.ADMIN;
    }
}
