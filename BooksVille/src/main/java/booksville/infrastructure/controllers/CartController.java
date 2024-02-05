package booksville.infrastructure.controllers;

import booksville.payload.response.ApiResponse;
import booksville.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse<String>> addToCart(@RequestParam Long userId,
                                                         @RequestParam Long bookId,
                                                         @RequestParam Long quantity){
        cartService.addToCart(userId, bookId, quantity);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Book added to cart successful", "Book Added"));
    }

    @DeleteMapping("/removeFromCart")
    public ResponseEntity<ApiResponse<String>> removeFromCart(@RequestParam Long userId,
                                                              @RequestParam Long bookId){
        cartService.removeFromCart(userId, bookId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Book removed from cart successful", "Book Removed"));
    }
}
