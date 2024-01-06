package BooksVille.services.implementation;

import BooksVille.entities.model.BookEntity;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.repositories.BookRepository;
import BooksVille.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    @Override
    public ResponseEntity<ApiResponse<List<BookEntityResponse>>> searchBooks(String query) {
        List<BookEntity> bookSearch = bookRepository.searchBook(query);

        List<BookEntityResponse>searchResponses = bookSearch.stream()
                .map(bookEntity -> {
                    BookEntityResponse bookEntityResponse = modelMapper.map(bookEntity, BookEntityResponse.class);
                    bookEntityResponse.setBookTitle(bookEntity.getBookTitle());
                    bookEntityResponse.setAuthor(bookEntity.getAuthor());
                    bookEntityResponse.setGenre(bookEntity.getGenre());
                    bookEntityResponse.setPrice(bookEntity.getPrice());

                    return bookEntityResponse;
                })
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("search complete",searchResponses));
    }
}
