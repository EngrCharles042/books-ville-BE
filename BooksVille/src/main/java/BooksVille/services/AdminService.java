package BooksVille.services;

import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<ApiResponse<BookEntityResponse>> addBook(BookEntityRequest bookEntityRequest);
    ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseEntity<ApiResponse<List<BookEntityResponse>>> searchBooks(String query);
}
