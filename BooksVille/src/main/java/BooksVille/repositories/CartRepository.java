package BooksVille.repositories;

import BooksVille.entities.model.BookEntity;
import BooksVille.entities.model.CartEntity;
import BooksVille.entities.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findByUserAndBook(UserEntity user, BookEntity book);
    void deleteByUserAndBook(UserEntity user, BookEntity book);
}
