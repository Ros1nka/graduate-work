package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Role;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<AdEntity> result;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @Getter
    private boolean accountNonExpired = true;

    @Getter
    private boolean accountNonLocked = true;

    @Getter
    private boolean credentialsNonExpired = true;

    @Getter
    private boolean enabled = true;
}
