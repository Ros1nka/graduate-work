package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ad {

    //id автора объявления
    private int author;

    //ссылка на картинку объявления
    private String image;

    //id объявления
    private int pk;

    //цена объявления
    private int price;

    //заголовок объявления
    private String title;
}
