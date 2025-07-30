package ru.skypro.homework.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class UserDTO {

    //id пользователя'
    private int id;

    //логин пользователя'
    private String email;

    //имя пользователя'
    private String firstName;

    //фамилия пользователя'
    private String lastName;

    //телефон пользователя'
    private String phone;

    //роль пользователя'
    private Role role;

    //ссылка на аватар пользователя'
    private String image;
}