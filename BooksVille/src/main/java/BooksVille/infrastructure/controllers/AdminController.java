package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.services.AdminService;
import BooksVille.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/add-book")
    public ResponseEntity<ApiResponse<BookEntityResponse>> addBook(@Valid @RequestBody BookEntityRequest bookEntityRequest) {
        return adminService.addBook(bookEntityRequest);
    }


        return adminService.getAllBooks(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/search/title-or-author-or-price-or-genre")
    public ResponseEntity<ApiResponse<List<BookEntityResponse>>> bookSearchWithKeyword (@RequestParam("query")String query) {
        return adminService.searchBooks(query);
  
}
