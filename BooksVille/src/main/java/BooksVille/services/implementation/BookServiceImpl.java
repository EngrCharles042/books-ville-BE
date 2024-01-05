package BooksVille.services.implementation;

import BooksVille.entities.model.BookEntity;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.repositories.BookRepository;
import BooksVille.services.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse<BookEntityResponse>> findById(Long id) {
         BookEntity bookEntity = bookRepository
                 .findById(id)
                 .orElseThrow(
                        () -> new ApplicationException("Book not found")
                 );

         BookEntityResponse bookEntityResponse = modelMapper.map(bookEntity, BookEntityResponse.class);

         return ResponseEntity.ok(
                 new ApiResponse<>(
                         "successful",
                         bookEntityResponse
                 )
         );

    }


}
