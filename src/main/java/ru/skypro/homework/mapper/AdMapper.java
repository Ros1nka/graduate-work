package ru.skypro.homework.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

@Component
public class AdMapper {

    @Value("${path.images.ad}")
    private String imageDir;

    public Ad toAdDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }
        Ad dto = new Ad();

        dto.setPk(entity.getPk());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setImage(imageDir + entity.getPk());

        return dto;
    }

    public ExtendedAd toExtendedAdDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }
        ExtendedAd dto = new ExtendedAd();

        dto.setPk(entity.getPk());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getEmail());
        dto.setImage(entity.getImage() + "/extended");
        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());

        return dto;
    }

    public AdEntity toAdEntity(CreateOrUpdateAd dto, UserEntity author) {
        if (dto == null || author == null) {
            return null;
        }
        AdEntity entity = new AdEntity();

        entity.setAuthor(author);
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
