package BooksVille.payload.response.authResponse;

import BooksVille.entities.enums.Gender;
import BooksVille.entities.enums.Roles;
import lombok.*;

import javax.management.relation.Role;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponse {
    Long id;
    String firstName;
    String lastName;
    String profilePicture;
    String email;
    Gender gender;
    Roles role;
    String accessToken;
    String refreshToken;
    String tokenType = "Bearer";
}
