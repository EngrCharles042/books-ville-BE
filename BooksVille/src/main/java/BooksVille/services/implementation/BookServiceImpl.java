package BooksVille.services.implementation;

import BooksVille.entities.enums.Genre;
import BooksVille.entities.model.BookEntity;
import BooksVille.entities.model.SavedBooksEntity;
import BooksVille.entities.model.UserEntity;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.repositories.BookRepository;
import BooksVille.repositories.SavedBooksEntityRepository;
import BooksVille.repositories.UserEntityRepository;
import BooksVille.services.BookService;
import BooksVille.services.FileUpload;
import BooksVille.utils.FileUtils;
import BooksVille.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final SavedBooksEntityRepository savedBooksEntityRepository;
    private final ModelMapper modelMapper;
    private final HelperClass helperClass;
    private final JWTGenerator jwtGenerator;
    private final UserEntityRepository userEntityRepository;
    private final HttpServletRequest request;
    private final FileUpload fileUpload;

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
                .map(bookEntityResponse -> modelMapper
                        .map(bookEntityResponse, BookEntityResponse.class))
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
    public ResponseEntity<ApiResponse<BookEntityResponse>> addBook(BookEntityRequest bookEntityRequest) throws IOException {
        BookEntity bookEntity = BookEntity.builder()
                .author(bookEntityRequest.getAuthor())
                .bookTitle(bookEntityRequest.getBookTitle())
                .genre(bookEntityRequest.getGenre())
                .description(bookEntityRequest.getDescription())
                .bookCover(fileUpload.uploadFile(bookEntityRequest.getBookCover()))
                .bookData(FileUtils.compressImage(bookEntityRequest.getBookFile().getBytes()))
                .price(bookEntityRequest.getPrice())
                .build();

        BookEntity savedBook = bookRepository.save(bookEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "success",
                                modelMapper.map(savedBook, BookEntityResponse.class)
                        )
                );
    }

    @Override
    public byte[] downloadImage(Long id) {
        Optional<BookEntity> dbFileData = bookRepository.findById(id);

        return FileUtils.decompressImage(dbFileData.get().getBookData());
    }

    @Override
    public ResponseEntity<ApiResponse<BookEntityResponse>> editBook(BookEntityRequest bookEntityRequest, Long bookEntityId) {
        UserEntity existingAdmin = getCurrentUserFromToken();

        Optional<BookEntity> optionalBookEntity = bookRepository.findBookEntitiesById(bookEntityId);

        if(optionalBookEntity.isEmpty()){
            throw new ApplicationException("Book with id " +bookEntityId+ " does not exist");
        }

        BookEntity existingBook = optionalBookEntity.get();
        existingBook.setAuthor(bookEntityRequest.getAuthor());
        existingBook.setBookTitle(bookEntityRequest.getBookTitle());
        existingBook.setGenre(bookEntityRequest.getGenre());
        existingBook.setDescription(bookEntityRequest.getDescription());
        existingBook.setPrice(bookEntityRequest.getPrice());

        bookRepository.save(existingBook);

        BookEntityResponse bookEntityResponse = BookEntityResponse.builder()
                .id(existingBook.getId())
                .author(existingBook.getAuthor())
                .bookTitle(existingBook.getBookTitle())
                .genre(existingBook.getGenre())
                .description(existingBook.getDescription())
                .build();
        return ResponseEntity.ok(new ApiResponse<>("edited successfully",bookEntityResponse));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteBook(Long bookId) {
        Optional<BookEntity> optionalBookEntity = bookRepository.findBookEntitiesById(bookId);

        if(optionalBookEntity.isEmpty()){
            throw new ApplicationException("Book with id " +bookId+ " does not exist");
        }

        BookEntity existingBook = optionalBookEntity.get();

        bookRepository.delete(existingBook);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Book with id " +bookId+ " deleted"
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> hideBook(Long bookId) {
        Optional<BookEntity> optionalBookEntity = bookRepository.findBookEntitiesById(bookId);
        if(optionalBookEntity.isEmpty()){
            throw new ApplicationException("Book with id " +bookId+ " does not exist");
        }
        BookEntity existingBook = optionalBookEntity.get();
        existingBook.setHidden(true);
        bookRepository.save(existingBook);

        return ResponseEntity.ok(new ApiResponse<>("Book with id " + bookId + " is now hidden"));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> saveBook(Long id) {
        BookEntity book = bookRepository.
                findById(id)
                .orElseThrow(
                    () -> new ApplicationException("Book not found with id: " + id)
                );

        UserEntity userEntity = getCurrentUserFromToken();

        savedBooksEntityRepository.save(
                SavedBooksEntity.builder()
                        .bookEntity(book)
                        .userEntity(userEntity)
                        .build()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "Book saved successfully",
                                "success"
                        )
                );
    }

    @Override
    public ResponseEntity<ApiResponse<BookResponsePage>> getSavedBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Sort condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<SavedBooksEntity> savedBooksEntitiesPage = savedBooksEntityRepository
                .findSavedBooksEntitiesByUserEntity(getCurrentUserFromToken(), pageable);

        List<SavedBooksEntity> bookEntities = savedBooksEntitiesPage.getContent();

        List<BookEntityResponse> bookEntityResponses = bookEntities.stream()
                .map(bookEntityResponse -> modelMapper
                        .map(bookEntityResponse.getBookEntity(), BookEntityResponse.class))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Success",
                        BookResponsePage.builder()
                                .content(bookEntityResponses)
                                .pageNo(savedBooksEntitiesPage.getNumber())
                                .pageSize(savedBooksEntitiesPage.getSize())
                                .totalElements(savedBooksEntitiesPage.getTotalElements())
                                .totalPages(savedBooksEntitiesPage.getTotalPages())
                                .last(savedBooksEntitiesPage.isLast())
                                .build()
                )
        );
    }

    private UserEntity getCurrentUserFromToken() {
        String token = helperClass.getTokenFromHttpRequest(request);
        String email = jwtGenerator.getEmailFromJWT(token);
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("Invalid token or authentication issue"));
    }
}
