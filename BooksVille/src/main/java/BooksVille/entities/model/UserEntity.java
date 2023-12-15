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
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @Transient
    private String confirmPassword;

    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String dob;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    private Boolean isVerified;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities = new ArrayList<>();
}
