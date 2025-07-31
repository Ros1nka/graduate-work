package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {

    @Value("${path.images.ad}")
    private String imageDir;

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final ImageServiceImpl imageServiceImpl;


    public AdsServiceImpl(AdRepository adRepository,
                          AdMapper adMapper,
                          UserRepository userRepository,
                          ImageServiceImpl imageServiceImpl) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
        this.imageServiceImpl = imageServiceImpl;
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

    @Transactional
    @Override
    public Ad createAd(CreateOrUpdateAd properties, MultipartFile image, String username)
            throws IOException {

        UserEntity author = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        AdEntity adEntity = adMapper.toAdEntity(properties, author);

        AdEntity savedEntity = adRepository.save(adEntity);

        imageServiceImpl.uploadAndSaveImage(imageDir, String.valueOf(savedEntity.getPk()), image);

        savedEntity.setImage(imageDir + savedEntity.getPk() + imageServiceImpl.getFileExtension(image.getOriginalFilename()));

        adRepository.save(savedEntity);

        return adMapper.toAdDto(savedEntity);
    }

    @Override
    public ExtendedAd getAds(int adId) {

        AdEntity adEntity = adRepository.findByPk(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        return adMapper.toExtendedAdDto(adEntity);
    }

    @Override
    public void deleteAd(int adId, String username) throws AdNotFoundException, ForbiddenException {

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        AdEntity adEntity = adRepository.findByPk(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        adRepository.delete(adEntity);
    }

    @Override
    public Ad updateAd(int adId, CreateOrUpdateAd updatedAd, String username)
            throws AdNotFoundException, ForbiddenException {

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        AdEntity adEntity = adRepository.findByPk(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        adEntity.setTitle(updatedAd.getTitle());
        adEntity.setPrice(updatedAd.getPrice());
        adEntity.setDescription(updatedAd.getDescription());

        AdEntity updatedEntity = adRepository.save(adEntity);

        return adMapper.toAdDto(updatedEntity);
    }

    @Override
    public Ads getAdsByAuthor(String username) {

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<AdEntity> ads = adRepository.findAllByAuthorId(user.getId());

        List<Ad> adDTOs = ads.stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        return new Ads(adDTOs.size(), adDTOs);
    }

    @Override
    public byte[] updateAdImage(int adId, MultipartFile image, String username)
            throws AdNotFoundException, ForbiddenException, IOException {

        AdEntity adEntity = adRepository.findByPk(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        byte[] imageBytes = imageServiceImpl.uploadAndSaveImage(imageDir, String.valueOf(adId), image);

        adEntity.setImage(imageDir + adId + imageServiceImpl.getFileExtension(image.getOriginalFilename()));
        adRepository.save(adEntity);

        return imageBytes;
    }

    public boolean isAdAuthorOrAdmin(int adId, String username) {

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ad.getAuthor().getId() == user.getId() || user.getRole() == Role.ADMIN;
    }
}
