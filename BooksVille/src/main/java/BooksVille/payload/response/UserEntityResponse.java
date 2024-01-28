package BooksVille.payload.response;

import BooksVille.entities.enums.Gender;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link BooksVille.entities.model.UserEntity}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntityResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private String phoneNumber;
    private String gender;
}