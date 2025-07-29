package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Register {

    @NotBlank(message = "Логин обязателен")
    @Size(min = 4, max = 32, message = "Логин должен быть от 4 до 32 символов")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String password;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 16, message = "Имя должно быть от 2 до 16 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 16, message = "Фамилия должна быть от 2 до 16 символов")
    private String lastName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @NotNull(message = "Роль обязательна")
    private Role role;
}
