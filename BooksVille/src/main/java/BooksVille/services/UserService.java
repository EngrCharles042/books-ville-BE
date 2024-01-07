package BooksVille.services;

import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ApiResponse<List<BookEntityResponse>>> searchBooks(String query);
}
