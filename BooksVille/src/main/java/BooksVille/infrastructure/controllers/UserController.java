package BooksVille.infrastructure.controllers;

import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final BookService bookService;

    @GetMapping("/get-book")
    public ResponseEntity<ApiResponse<BookEntityResponse>> findById (@RequestParam Long id) {
    return bookService.findById(id);

    }
}
