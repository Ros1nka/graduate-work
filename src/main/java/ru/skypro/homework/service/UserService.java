package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;

import java.io.IOException;

public interface UserService {
    boolean changePassword(String username, String currentPassword, String newPassword);

    UserDTO getUser(String username);

    UpdateUser updateUser(String name, UpdateUser updateUser);

    void updateUserAvatar(String name, MultipartFile image)
            throws IOException;
}
