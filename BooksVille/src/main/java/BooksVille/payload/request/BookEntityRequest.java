package BooksVille.payload.request;

import BooksVille.entities.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link BooksVille.entities.model.BookEntity}
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookEntityRequest {

    @Size(min = 2, max = 40, message = "Author's name is too long or short")
    @NotBlank(message = "Name cannot be blank")
    private  String author;

    @NotBlank(message = "Title title is required.")
    @Size(min = 10, max = 100)
    private  String bookTitle;

    private  Genre genre;

    @NotBlank(message = "Book description is required.")
    @Size(min = 10, max = 1000)
    private  String description;

    @NotNull(message = "Book price must be declared.")
    private  BigDecimal price;
}
