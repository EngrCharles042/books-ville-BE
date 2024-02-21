package booksville.repositories;

import booksville.entities.model.BookEntity;
import booksville.entities.model.TransactionEntity;
import booksville.entities.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByUserEntityAndBookEntity(UserEntity userEntity, BookEntity bookEntity);
    Page<TransactionEntity> findAllByUserEntity(UserEntity userEntity, Pageable pageable);
}