package ru.skypro.homework.dto;

import lombok.Data;
import java.util.List;

@Data
public class Comments {
    //общее количество комментариев'
    private int count;

    private List<Comment> results;
}