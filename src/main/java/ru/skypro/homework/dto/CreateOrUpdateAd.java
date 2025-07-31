package ru.skypro.homework.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateAd {

    @NotBlank(message = "Заголовок объявления обязателен")
    @Size(min = 4, max = 32, message = "Заголовок должен быть от 4 до 32 символов")
    private String title;

    @NotNull(message = "Цена объявления обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Max(value = 10000000, message = "Цена не может превышать 10 000 000")
    private int price;

    @NotBlank(message = "Описание объявления обязательно")
    @Size(min = 8, max = 64, message = "Описание должно быть от 8 до 64 символов")
    private String description;
}