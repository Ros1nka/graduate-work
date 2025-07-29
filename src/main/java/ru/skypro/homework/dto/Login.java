package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {

    @NotBlank(message = "Логин обязателен")
    @Size(min = 4, max = 32, message = "Логин должен быть от 4 до 32 символов")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String password;
}
