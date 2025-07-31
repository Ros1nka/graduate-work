package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ImageService imageService;

    @InjectMocks
    UserServiceImpl userService;

    private final String testEmail = "user@example.com";
    private final String testPassword = "password";
    private final UserEntity testUser = new UserEntity(
            1,
            testEmail,
            "Иван",
            "Иванов",
            "+79991112233",
            testPassword,
            Role.USER,
            null,
            null,
            null,
            true,
            true,
            true,
            true);

    @Test
    void changePassword_MustReturnsTrue() {

        String currentPassword = "oldPassword";
        String newPassword = "newPassword";

        when(userRepository.findByEmail(testEmail))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(currentPassword, testPassword))
                .thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("password");

        boolean result = userService.changePassword(testEmail, currentPassword, newPassword);

        assertTrue(result);
        verify(userRepository).save(testUser);
        assertEquals("password", testUser.getPassword());
    }

    @Test
    void getUser_WhenUserExists_ReturnsUserDTO() {

        UserDTO expectedDto = new UserDTO();
        expectedDto.setEmail(testEmail);

        when(userRepository.findByEmail(testEmail))
                .thenReturn(Optional.of(testUser));
        when(userMapper.toUserDto(testUser))
                .thenReturn(expectedDto);

        UserDTO result = userService.getUser(testEmail);

        assertEquals(expectedDto, result);
        verify(userMapper).toUserDto(testUser);
    }

    @Test
    void updateUser_WhenValidData_ReturnsUpdatedUser() {
        // Arrange
        UpdateUser updateDto = new UpdateUser();
        updateDto.setFirstName("Updated");
        updateDto.setLastName("Name");
        updateDto.setPhone("+79999999999");

        UserEntity savedUser = new UserEntity();
        savedUser.setFirstName("Updated");

        UpdateUser expectedDto = new UpdateUser();
        expectedDto.setFirstName("Updated");

        when(userRepository.findByEmail(testEmail))
                .thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(savedUser);
        when(userMapper.toUpdateUserDto(savedUser))
                .thenReturn(expectedDto);

        // Act
        UpdateUser result = userService.updateUser(testEmail, updateDto);

        // Assert
        assertEquals("Updated", result.getFirstName());
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUserAvatar_WhenEmptyImage_ThrowsException() {
        MultipartFile emptyImage = mock(MultipartFile.class);
        when(emptyImage.isEmpty()).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserAvatar(testEmail, emptyImage));
    }
}
