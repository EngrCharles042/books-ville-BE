package BooksVille.services;

import BooksVille.payload.request.BookFilterRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ApiResponse<List<BookEntityResponse>>> searchBooks(String query);

    ResponseEntity<ApiResponse<BookResponsePage>> filterBooks(
            int pageNo, int pageSize, String sortBy, String sortDir, BookFilterRequest bookFilterRequest
    );
}
