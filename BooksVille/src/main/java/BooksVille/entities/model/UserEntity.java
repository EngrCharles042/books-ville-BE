package BooksVille.entities.model;

import BooksVille.entities.enums.Gender;
import BooksVille.entities.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users_table")
public class UserEntity extends BaseEntity{

    @Column(nullable = false,length = 20)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(nullable = false,length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 11)
    private String dob;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    private Boolean isVerified;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities = new ArrayList<>();
}
