package BooksVille.services;

import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import org.springframework.http.ResponseEntity;

public interface BookService {
    ResponseEntity<ApiResponse<BookEntityResponse>> findById(Long id);
    ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir);
}
