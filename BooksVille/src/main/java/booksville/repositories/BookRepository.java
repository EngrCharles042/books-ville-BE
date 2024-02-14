package booksville.repositories;

import booksville.entities.enums.Genre;
import booksville.entities.model.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findById(Long id);
    Optional<BookEntity> findByBookTitleAndAuthor(String bookTitle, String author);
    @Query(value = "SELECT * FROM book_entity " +
            "WHERE LOWER(bookTitle) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(author) LIKE LOWER(CONCAT('%', :query, '%'))"+
            "OR LOWER(genre) LIKE LOWER(CONCAT('%', :query, '%'))"+
            "OR LOWER(price) LIKE LOWER(CONCAT('%', :query, '%'))",
            nativeQuery = true)
    List<BookEntity> searchBook(@Param("query") String query);

    Optional<BookEntity> findBookEntitiesById(Long bookEntityId);

    Page<BookEntity> findBookEntitiesByGenreContainingAndRating(Genre genre, Integer rating, Pageable pageable);

    @Query(value = """
            select b from BookEntity b
            where upper(b.author) like upper(concat('%', :query, '%')) 
            or upper(b.bookTitle) like upper(concat('%', :query, '%')) 
            or upper(b.genre) like upper(concat('%', :query, '%'))""", nativeQuery = true)
    Page<BookEntity> searchUsingAuthorOrTitleOrGenre (@Param("query") String query, Pageable pageable);
}
