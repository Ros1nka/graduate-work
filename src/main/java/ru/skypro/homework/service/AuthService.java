package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDTO;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);
}
