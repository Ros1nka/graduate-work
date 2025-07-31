package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Comments {
    //общее количество комментариев'
    private int count;

    private List<Comment> results;
}