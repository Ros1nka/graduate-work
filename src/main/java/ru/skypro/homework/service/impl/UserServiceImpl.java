package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.mapper.UserMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${path.images.users}")
    private String imageDir;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword) {

        UserEntity user = userRepository.findByEmail(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDTO getUser(String username) {

        UserEntity user = userRepository.findByEmail(username);

        return userMapper.toUserDto(user);
    }

    @Override
    public UpdateUser updateUser(String username, UpdateUser updateUser) {

        UserEntity user = userRepository.findByEmail(username);

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());

        UserEntity updatedUser = userRepository.save(user);

        return userMapper.toUpdateUserDto(updatedUser);
    }

    @Override
    public void updateUserAvatar(String username, MultipartFile image) {

        UserEntity user = userRepository.findByEmail(username);

        user.setImage("");  //TO DO image
        userRepository.save(user);
    }
}
