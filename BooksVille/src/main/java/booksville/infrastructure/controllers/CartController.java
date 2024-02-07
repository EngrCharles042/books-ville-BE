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
    public ResponseEntity<ApiResponse<String>> addToCart(@RequestParam Long id) {

        return cartService.addToCart(id);
    }

    @DeleteMapping("/removeFromCart")
    public ResponseEntity<ApiResponse<String>> removeFromCart(@RequestParam Long id) {

        return cartService.removeFromCart(id);
    }
}
