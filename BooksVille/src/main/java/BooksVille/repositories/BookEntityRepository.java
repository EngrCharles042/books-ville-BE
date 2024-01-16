package BooksVille.repositories;

import BooksVille.entities.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEntityRepository extends JpaRepository<BookEntity, Long> {
}