package BooksVille.entities.model;

import BooksVille.entities.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_entity")
public class BookEntity extends BaseEntity{
    private String author;

    private String bookTitle;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String description;

    private String shortDescription;

    private BigDecimal price;

    private Boolean hidden = false;

    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions = new ArrayList<>();

    private Integer rating;
}
