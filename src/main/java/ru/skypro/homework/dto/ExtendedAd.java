package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedAd {

    //id объявления'
    int pk;

    //имя автора объявления'
    String authorFirstName;

    //фамилия автора объявления'
    String authorLastName;

    //описание объявления'
    String description;

    //логин автора объявления'
    String email;

    //ссылка на картинку объявления'
    String image;

    //телефон автора объявления'
    String phone;

    //цена объявления'
    int price;

    //заголовок объявления'
    String title;
}
