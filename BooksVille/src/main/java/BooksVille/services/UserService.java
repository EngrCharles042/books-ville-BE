package BooksVille.services;

import BooksVille.entities.model.UserEntity;
import BooksVille.payload.request.BookFilterRequest;
import BooksVille.payload.request.UserEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserEntity getUserEntity();
    ResponseEntity<ApiResponse<List<BookEntityResponse>>> searchBooks(String query);

    ResponseEntity<ApiResponse<BookResponsePage>> filterBooks(
            int pageNo, int pageSize, String sortBy, String sortDir, BookFilterRequest bookFilterRequest
    );

    ResponseEntity<ApiResponse<String>> userInfoUpdate(UserEntityRequest userEntityRequest);
}
