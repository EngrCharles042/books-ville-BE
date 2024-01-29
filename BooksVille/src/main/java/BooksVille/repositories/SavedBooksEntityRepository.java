package BooksVille.repositories;

import BooksVille.entities.model.SavedBooksEntity;
import BooksVille.entities.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedBooksEntityRepository extends JpaRepository<SavedBooksEntity, Long> {
    Page<SavedBooksEntity> findSavedBooksEntitiesByUserEntity(UserEntity userEntity, Pageable pageable);
}