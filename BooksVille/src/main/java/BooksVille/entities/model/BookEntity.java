package BooksVille.entities.model;

import BooksVille.entities.enums.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_entity")
public class BookEntity extends BaseEntity{
    @Column(nullable = false,length = 45)
    private String author;

    @Column(length = 35)
    private String bookTitle;

    @Column(length = 35)
    private Genre genre;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 200)
    private String shortDescription;

    @Column(nullable = false, length = 50)
    private BigDecimal price;
}
