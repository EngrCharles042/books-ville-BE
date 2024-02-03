package BooksVille.repositories;

import BooksVille.entities.model.BookEntity;
import BooksVille.entities.model.TransactionEntity;
import BooksVille.entities.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByUserEntityAndBookEntity(UserEntity userEntity, BookEntity bookEntity);

}