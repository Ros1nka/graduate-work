package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> setPassword(@Valid @RequestBody NewPassword newPassword,
                                         Authentication auth) {

        if (userService.changePassword(auth.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUser(Authentication auth) {

        UserDTO user = userService.getUser(auth.getName());

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser,
                                                 Authentication auth) {

        UpdateUser updatedUser = userService.updateUser(auth.getName(), updateUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserImage(Authentication auth,
                                             @RequestParam MultipartFile image) throws IOException {

        userService.updateUserAvatar(auth.getName(), image);

        return ResponseEntity.ok().build();
    }
}