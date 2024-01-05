package BooksVille.repositories;

import BooksVille.entities.model.BookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


import java.awt.print.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findById(Long id);
    Optional<BookEntity> findByBookTitleAndAuthor(String bookTitle, String author);
}
