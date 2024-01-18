package BooksVille.services;

import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BookService {
    ResponseEntity<ApiResponse<BookEntityResponse>> findById(Long id);
    ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseEntity<ApiResponse<BookEntityResponse>> addBook(BookEntityRequest bookEntityRequest) throws IOException;
    byte[] downloadImage(Long id);
    ResponseEntity<ApiResponse<BookEntityResponse>> editBook(BookEntityRequest bookEntityRequest, Long bookEntityId);
    ResponseEntity<ApiResponse<String>> deleteBook(Long bookId);
    ResponseEntity<ApiResponse<String>> hideBook(Long bookId);
}
