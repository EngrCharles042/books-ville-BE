package BooksVille.payload.response;

import BooksVille.entities.enums.Genre;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link BooksVille.entities.model.BookEntity}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEntityResponse {
    private  Long id;
    private  String author;
    private  String bookTitle;
    private  Genre genre;
    private  String shortDescription;
    private  BigDecimal price;
}