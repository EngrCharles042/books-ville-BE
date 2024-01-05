package BooksVille.services.implementation;

import BooksVille.entities.model.BookEntity;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.repositories.BookRepository;
import BooksVille.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;

    @Override
    public ResponseEntity<ApiResponse<BookEntityResponse>> addBook(BookEntityRequest bookEntityRequest) {
        Optional<BookEntity> bookEntityOptional = bookRepository
                .findByBookTitleAndAuthor(bookEntityRequest.getBookTitle(), bookEntityRequest.getAuthor());

        if (bookEntityOptional.isPresent()) {
            throw new ApplicationException("Book Already Exist");
        }

        BookEntity newBook = bookRepository.save(modelMapper.map(bookEntityRequest, BookEntity.class));

        BookEntityResponse bookResponse = modelMapper.map(newBook, BookEntityResponse.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "Book created successfully",
                                bookResponse
                        )
                );
    }

    @Override
    public ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Sort condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BookEntity> bookEntitiesPage = bookRepository.findAll(pageable);

        List<BookEntity> bookEntities = bookEntitiesPage.getContent();

        List<BookEntityResponse> bookEntityResponses = bookEntities.stream()
                .map(bookEntityResponse -> modelMapper.map(bookEntityResponse, BookEntityResponse.class))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Success",
                        BookResponsePage.builder()
                                .content(bookEntityResponses)
                                .pageNo(bookEntitiesPage.getNumber())
                                .pageSize(bookEntitiesPage.getSize())
                                .totalElements(bookEntitiesPage.getTotalElements())
                                .totalPages(bookEntitiesPage.getTotalPages())
                                .last(bookEntitiesPage.isLast())
                                .build()
                )
        );
    }

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
