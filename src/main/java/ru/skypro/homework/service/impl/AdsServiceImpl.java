package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public AdsServiceImpl(AdRepository adRepository,
                          AdMapper adMapper,
                          UserService userService,
                          UserRepository userRepository) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Ads getAllAds(String username) {

        List<Ad> adDTOs = adRepository.findAll().stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        Ads adsContainer = new Ads();
        adsContainer.setCount(adDTOs.size());
        adsContainer.setResults(adDTOs);

        return adsContainer;
    }

    @Override
    public Ad createAd(CreateOrUpdateAd properties, MultipartFile image, String username) {

        UserEntity author = userRepository.findByEmail(username);

        AdEntity adEntity = adMapper.toAdEntity(properties, author);

        AdEntity savedEntity = adRepository.save(adEntity);

        return adMapper.toAdDto(savedEntity);
    }

    @Override
    public ExtendedAd getAds(int adId) {

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        return adMapper.toExtendedAdDto(adEntity);
    }

    @Override
    public void deleteAd(int adId, String username) throws AdNotFoundException, ForbiddenException {

        UserEntity user = userRepository.findByEmail(username);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        if (adEntity.getAuthor().getId() != user.getId() && !user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException("You are not allowed to delete this ad");
        }
        adRepository.delete(adEntity);
    }

    @Override
    public Ad updateAd(int adId, CreateOrUpdateAd updatedAd, String username) throws AdNotFoundException, ForbiddenException {

        UserEntity user = userRepository.findByEmail(username);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        if (adEntity.getAuthor().getId() != (user.getId())) {
            throw new ForbiddenException("You are not allowed to update this ad");
        }
        adEntity.setTitle(updatedAd.getTitle());
        adEntity.setPrice(updatedAd.getPrice());
        adEntity.setDescription(updatedAd.getDescription());

        AdEntity updatedEntity = adRepository.save(adEntity);

        return adMapper.toAdDto(updatedEntity);
    }

    @Override
    public Ads getAdsByAuthor(String username) {

        UserEntity user = userRepository.findByEmail(username);

        List<AdEntity> ads = adRepository.findAllByAuthorId(user.getId());

        List<Ad> adDTOs = ads.stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        return new Ads(adDTOs.size(), adDTOs);
    }

    @Override
    public byte[] updateAdImage(int adId, MultipartFile image, String username) throws AdNotFoundException, ForbiddenException {

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        UserEntity user = userRepository.findByEmail(username);

        if (adEntity.getAuthor().getId() != user.getId() && !user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException("You are not allowed to update this ad");
        }

        adEntity.setImage("");  //TO DO image
        adRepository.save(adEntity);
        return null;
    }
}
