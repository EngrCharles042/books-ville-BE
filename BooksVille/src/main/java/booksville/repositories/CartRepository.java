package booksville.repositories;

import booksville.entities.model.BookEntity;
import booksville.entities.model.CartEntity;
import booksville.entities.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findByUserEntityAndBookEntity(UserEntity user, BookEntity book);
    void deleteByUserEntityAndBookEntity(UserEntity user, BookEntity book);
}
