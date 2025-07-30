package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.UserDTO;

public interface UserRepository extends JpaRepository<UserDTO, Long> {

    UserDTO findByUsername(String name);
}
