package ru.skypro.homework.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class Comment {

    //id автора комментария
    private int author;

    //ссылка на аватар автора комментария'
    private String authorImage;

    //имя создателя комментария'
    private String authorFirstName;

    //дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970'
    private int createdAt;

    //id комментария'
    private int pk;

    //текст комментария'
    private String text;
}
