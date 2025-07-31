package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.UserEntity;

public class UserMapper {


    public UserDTO toUserDto(UserEntity entity) {

        UserDTO dto = new UserDTO();

        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setImage("/image/users/" + entity.getId());

        return dto;
    }

    public UpdateUser toUpdateUserDto(UserEntity entity) {

        UpdateUser dto = new UpdateUser();

        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());

        return dto;
    }
}
