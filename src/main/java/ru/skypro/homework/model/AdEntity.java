package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skypro.homework.dto.Comment;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "ads")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private int pk;

    @Column(name = "title", nullable = false, length = 32)
    private String title;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "description",length = 64)
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;
}
