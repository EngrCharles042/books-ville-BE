package booksville.services.implementation;

import booksville.entities.model.BookEntity;
import booksville.entities.model.CartEntity;
import booksville.entities.model.UserEntity;
import booksville.repositories.BookRepository;
import booksville.repositories.CartRepository;
import booksville.repositories.UserEntityRepository;
import booksville.services.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserEntityRepository userEntityRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void addToCart(Long userId, Long bookId, Long quantity) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new IllegalArgumentException("Book not found"));

        CartEntity existingCartItem = cartRepository.findByUserAndBook(user, book );

        if (existingCartItem != null){
            //adds multiple quantity of book to cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartRepository.save(existingCartItem);
        } else {
            //adds fresh book if it's not in cart
            CartEntity newCartItem = new CartEntity();
            newCartItem.setUser(user);
            newCartItem.setBook(book);
            newCartItem.setQuantity(quantity);
            cartRepository.save(newCartItem);
        }
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long bookId) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new IllegalArgumentException("Book not found"));

        CartEntity checkCart = cartRepository.findByUserAndBook(user, book);
        if (checkCart == null){
            throw new IllegalArgumentException("No book in cart");
        }

        cartRepository.deleteByUserAndBook(user, book);
    }
}
