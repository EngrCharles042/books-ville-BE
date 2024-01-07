package BooksVille.infrastructure.controllers;

import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search/title-or-author-or-price-or-genre")
    public ResponseEntity<ApiResponse<List<BookEntityResponse>>> bookSearchWithKeyword (@RequestParam("query")String query) {
        return userService.searchBooks(query);
    }
}
