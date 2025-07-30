package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrUpdateComment {

    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = 8, max = 64, message = "Комментарий должен содержать от 8 до 64 символов")
    private String text;
}
