package BooksVille.repositories;

import BooksVille.entities.enums.Genre;
import BooksVille.entities.model.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.awt.print.Book;

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



}
