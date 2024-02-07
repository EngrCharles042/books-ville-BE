package booksville.services;

import booksville.payload.request.BookEntityRequest;
import booksville.payload.response.ApiResponse;
import booksville.payload.response.BookEntityResponse;
import booksville.payload.response.BookResponsePage;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface BookService {
    ResponseEntity<ApiResponse<BookEntityResponse>> findById(Long id);
    ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseEntity<ApiResponse<BookEntityResponse>> addBook(BookEntityRequest bookEntityRequest) throws IOException;
    ResponseEntity<ApiResponse<BookEntityResponse>> editBook(BookEntityRequest bookEntityRequest, Long bookEntityId);
    ResponseEntity<ApiResponse<String>> deleteBook(Long bookId);
    ResponseEntity<ApiResponse<String>> hideBook(Long bookId);
    ResponseEntity<ApiResponse<BookResponsePage>> getSavedBooks(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseEntity<ApiResponse<String>> saveBook(Long id);
    byte[] downloadBook (Long bookId);
    ResponseEntity<ApiResponse<BookResponsePage>> getPurchasedBooks(int pageNo, int pageSize, String sortBy, String sortDir);
}
