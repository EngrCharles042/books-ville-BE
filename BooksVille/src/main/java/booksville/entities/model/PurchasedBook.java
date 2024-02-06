package booksville.entities.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_Purchased")
public class PurchasedBook extends BaseEntity {

    private String author;

    private String bookTitle;

    private String genre;

    @Column(length = 2500)
    private String description;

    private String bookCover;

    @Lob
    @Column(length = 2500)
    private byte[] bookData;

    private BigDecimal price;

    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL)
    private List<SavedBooksEntity> savedBooksEntities = new ArrayList<>();

    private Integer rating;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "Book_id")
    private BookEntity book;
}
