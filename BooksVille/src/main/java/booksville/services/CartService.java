package booksville.services;

import booksville.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<ApiResponse<String>> addToCart(Long bookId);
    ResponseEntity<ApiResponse<String>> removeFromCart(Long bookId);
}
