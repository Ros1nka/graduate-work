package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Ads;

public interface AdRepository extends JpaRepository<Ads, Integer> {
}
