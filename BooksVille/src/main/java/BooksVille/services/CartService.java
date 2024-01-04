package BooksVille.services;

public interface CartService {
    void addToCart(Long userId, Long bookId, Long quantity);
    void removeFromCart(Long userId, Long bookId);
}
