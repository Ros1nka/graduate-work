package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    private final CommentService commentService;

    //'Получение всех объявлений
    @GetMapping()
    public ResponseEntity<Ads> getAllAds(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Ads result = adsService.getAllAds(auth.getName());
        return ResponseEntity.ok(result);
    }

    //Добавление объявления
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestParam MultipartFile image,
                                    @Valid CreateOrUpdateAd properties,
                                    Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Ad createdAd = adsService.createAd(properties, image, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    //Получение комментариев объявления
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable int id,
                                                Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Comments comments = commentService.getComments(id);
            return ResponseEntity.ok(comments);
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Добавление комментария к объявлению
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable int id,
                                              @Valid @RequestBody CreateOrUpdateComment comment,
                                              Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Comment createdComment = commentService.addComment(id, comment, auth.getName());
            return ResponseEntity.ok(createdComment);
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable int id,
                                             Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ExtendedAd extendedAd = adsService.getAds(id);
            return ResponseEntity.ok(extendedAd);
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<ExtendedAd> removeAd(@PathVariable int id,
                                               Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            adsService.deleteAd(id, auth.getName());
            return ResponseEntity.noContent().build();
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable int id,
                                        @Valid @RequestBody CreateOrUpdateAd updatedAd,
                                        Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Ad result = adsService.updateAd(id, updatedAd, auth.getName());
            return ResponseEntity.ok(result);
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Удаление комментария
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int adId,
                                              @PathVariable int commentId,
                                              Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            commentService.deleteComment(adId, commentId, auth.getName());
            return ResponseEntity.ok().build();
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Обновление комментария
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable int adId,
                                                 @PathVariable int commentId,
                                                 @Valid @RequestBody CreateOrUpdateComment updatedComment,
                                                 Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Comment comment = commentService.updateComment(adId, commentId, updatedComment, auth.getName());
            return ResponseEntity.ok().build();
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Ads ads = adsService.getAdsByAuthor(auth.getName());
        return ResponseEntity.ok(ads);
    }

    //Обновление картинки объявления
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable int id,
                                              @RequestParam MultipartFile image,
                                              Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            byte[] imageBytes = adsService.updateAdImage(id, image, auth.getName());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (AdNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
