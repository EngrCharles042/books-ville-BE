package BooksVille.payload.response;

import BooksVille.entities.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEntityResponse {
    private Long id;
    private LocalDateTime dateCreated;
    private String author;
    private String bookTitle;
    private String genre;
    private String description;
    private BigDecimal price;
    private String bookCover;
    private Boolean hidden;
    private Integer rating;
}