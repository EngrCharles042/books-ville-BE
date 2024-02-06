package booksville.repositories;


import booksville.entities.model.PurchasedBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedRepository extends JpaRepository<PurchasedBook, Long> {

    Page<PurchasedBook> findAllByUserEntity_EmailPurchasedByDateCreated(String userEntity_email, Pageable pageable);
}
