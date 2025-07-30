package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;

import java.util.List;

public interface AdsService {
    Ads getAllAds(String username);

    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, String username);

    ExtendedAd getAds(int AdId);

    void deleteAd(int id, String username) throws AdNotFoundException, ForbiddenException;

    Ad updateAd(int id, CreateOrUpdateAd updatedAd, String username) throws AdNotFoundException, ForbiddenException;

    Ads getAdsByAuthor(String username);

    byte[] updateAdImage(int AdId, MultipartFile image, String username) throws AdNotFoundException, ForbiddenException;
}
