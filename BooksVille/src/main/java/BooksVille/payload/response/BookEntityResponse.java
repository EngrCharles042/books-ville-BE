package BooksVille.payload.response;

import BooksVille.entities.enums.Genre;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link BooksVille.entities.model.BookEntity}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEntityResponse {
    private Long id;
    private Long userEntityId;
    private LocalDateTime dateCreated;
    private String author;
    private String bookTitle;
    private Genre genre;
    private  String description;
    private String shortDescription;
    private BigDecimal price;
}