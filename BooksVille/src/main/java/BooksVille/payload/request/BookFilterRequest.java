package BooksVille.payload.request;

import BooksVille.entities.enums.Budgets;
import BooksVille.entities.enums.Genre;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookFilterRequest {
    private Genre genre;

    private Integer rating;
}
