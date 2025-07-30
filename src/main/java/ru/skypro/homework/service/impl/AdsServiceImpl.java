package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;

import java.util.List;

@Service
public class AdsServiceImpl implements AdsService {

    AdRepository adRepository;

    public AdsServiceImpl(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @Override
    public List<Ads> getAllAds() {
        return List.of();
    }

    @Override
    public Ad createAd(CreateOrUpdateAd properties, MultipartFile image, String username) {
        return null;
    }

    @Override
    public Comments getComments(int id) {
        return null;
    }

    @Override
    public Comment addComment(int id, CreateOrUpdateComment updateComment, String username) {
        return null;
    }

    @Override
    public ExtendedAd getAds(int id) {
        return null;
    }

    @Override
    public void deleteAd(int id, String username) throws AdNotFoundException, ForbiddenException {

    }

    @Override
    public Ad updateAd(int id, CreateOrUpdateAd updatedAd, String username) throws AdNotFoundException, ForbiddenException {
        return null;
    }

    @Override
    public void deleteComment(int adId, int commentId, String username) throws AdNotFoundException, ForbiddenException {

    }

    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updatedComment, String username)
            throws AdNotFoundException, ForbiddenException {
        return null;
    }

    @Override
    public Ads getAdsByAuthor(String username) {
        return null;
    }

    @Override
    public byte[] updateAdImage(int AdId, MultipartFile image, String username) throws AdNotFoundException, ForbiddenException {
        return new byte[0];
    }
}
