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
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private Gender gender;
    private Roles role;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
