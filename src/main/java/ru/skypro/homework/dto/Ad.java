package ru.skypro.homework.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
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
