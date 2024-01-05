package BooksVille.services;

import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import org.springframework.http.ResponseEntity;

public interface BookService {
    ResponseEntity<ApiResponse<BookEntityResponse>> findById(Long id);
   // ResponseEntity<ApiResponse<BookEntityResponse>> findAll(int pageNo, int pageSize, String sortBy,String sortDir);
}
