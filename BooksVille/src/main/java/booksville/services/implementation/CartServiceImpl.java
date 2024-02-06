package booksville.services.implementation;

import booksville.entities.model.BookEntity;
import booksville.entities.model.CartEntity;
import booksville.entities.model.UserEntity;
import booksville.payload.response.ApiResponse;
import booksville.repositories.BookRepository;
import booksville.repositories.CartRepository;
import booksville.repositories.UserEntityRepository;
import booksville.services.CartService;
import booksville.utils.HelperClass;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final HelperClass helperClass;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> addToCart(Long bookId) {
        UserEntity user = helperClass.getUserEntity();

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new IllegalArgumentException("Book not found"));

        CartEntity existingCartItem = cartRepository.findByUserEntityAndBookEntity(user, book);

        if (existingCartItem != null) {

            return ResponseEntity.ok(
                    new ApiResponse<>(
                          "alreadyAdded"
                    )
            );

        } else {

            //adds fresh book if it's not in cart
            CartEntity newCartItem = new CartEntity();

            newCartItem.setUserEntity(user);
            newCartItem.setBookEntity(book);

            cartRepository.save(newCartItem);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            "success"
                    )
            );
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> removeFromCart(Long bookId) {
        UserEntity user = helperClass.getUserEntity();

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new IllegalArgumentException("Book not found"));

        CartEntity checkCart = cartRepository.findByUserEntityAndBookEntity(user, book);

        if (checkCart == null){
            throw new IllegalArgumentException("No book in cart");
        }

        cartRepository.deleteByUserEntityAndBookEntity(user, book);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success"
                )
        );
    }
}
