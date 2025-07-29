package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {
    //общее количество объявлений
    private int count;

    private List<Ad> results;
}
