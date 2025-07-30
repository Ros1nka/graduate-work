package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword) {

        return false;
    }

    @Override
    public UserDTO getUser(String username) {

        return null;
    }

    @Override
    public UpdateUser updateUser(String name, UpdateUser updateUser) {

        return null;
    }

    @Override
    public void updateUserAvatar(String name, MultipartFile image) {
    }


}
