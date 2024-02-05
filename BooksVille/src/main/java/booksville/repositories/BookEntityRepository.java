package booksville.repositories;

import booksville.entities.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEntityRepository extends JpaRepository<BookEntity, Long> {
}