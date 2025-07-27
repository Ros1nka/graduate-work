package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}