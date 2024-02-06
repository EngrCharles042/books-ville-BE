package booksville.services.implementation;

import booksville.entities.model.BookEntity;
import booksville.entities.model.SavedBooksEntity;
import booksville.entities.model.TransactionEntity;
import booksville.entities.model.UserEntity;
import booksville.infrastructure.exceptions.ApplicationException;
import booksville.infrastructure.security.JWTGenerator;
import booksville.payload.request.BookEntityRequest;
import booksville.payload.response.ApiResponse;
import booksville.payload.response.BookEntityResponse;
import booksville.payload.response.BookResponsePage;
import booksville.repositories.BookRepository;
import booksville.repositories.SavedBooksEntityRepository;
import booksville.repositories.TransactionEntityRepository;
import booksville.repositories.UserEntityRepository;
import booksville.services.BookService;
import booksville.services.FileUpload;
import booksville.utils.FileUtils;
import booksville.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final TransactionEntityRepository transactionEntityRepository;

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

        Optional<SavedBooksEntity> savedBooksEntity = savedBooksEntityRepository
                .findByUserEntityAndBookEntity(userEntity, book);

        if (savedBooksEntity.isPresent()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(
                            "alreadySaved"
                    )
            );
        }

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
    @Transactional
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
                        .map(bookEntityResponse.getBookEntity(), BookEntityResponse.class)
                )
                .toList();

        BookResponsePage bookResponsePage = BookResponsePage.builder()
                .content(bookEntityResponses)
                .pageNo(savedBooksEntitiesPage.getNumber())
                .pageSize(savedBooksEntitiesPage.getSize())
                .totalElements(savedBooksEntitiesPage.getTotalElements())
                .totalPages(savedBooksEntitiesPage.getTotalPages())
                .last(savedBooksEntitiesPage.isLast())
                .build();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Success",
                        bookResponsePage
                )
        );
    }

    private UserEntity getCurrentUserFromToken() {
        String token = helperClass.getTokenFromHttpRequest(request);
        String email = jwtGenerator.getEmailFromJWT(token);
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("Invalid token or authentication issue"));
    }

    public byte[] downloadBook (Long book_id){
        String email = jwtGenerator.getEmailFromJWT(helperClass.getTokenFromHttpRequest(request));

        UserEntity userEntity = userEntityRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User Not Found"));

        if(!userEntity.isVerified()){
            throw new ApplicationException("Email Not Verified");
        }

        BookEntity bookEntity = bookRepository.findById(book_id).orElseThrow(
                () -> new ApplicationException("Book Not Found")
        );

        Optional<TransactionEntity> transactions = transactionEntityRepository
                .findByUserEntityAndBookEntity(userEntity, bookEntity);

        if(transactions.isPresent()){
            BookEntity book = transactions.get().getBookEntity();

            return FileUtils.decompressImage(book.getBookData());

        } else {
            throw new ApplicationException("No completed transaction found. Please make a payment to download the book.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<BookResponsePage>> getPurchasedBooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        UserEntity userEntity = getCurrentUserFromToken();

        // Sort condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<TransactionEntity> transactionEntitiesPage = transactionEntityRepository
                .findAllByUserEntity(userEntity, pageable);

        List<TransactionEntity> purchasedBookEntities = transactionEntitiesPage.getContent();

        List<BookEntityResponse> bookEntityResponses = purchasedBookEntities.stream()
                .map(transactionEntity -> modelMapper
                        .map(transactionEntity.getBookEntity(), BookEntityResponse.class)
                ).toList();

        BookResponsePage bookResponsePage = BookResponsePage.builder()
                .content(bookEntityResponses)
                .pageNo(transactionEntitiesPage.getNumber())
                .pageSize(transactionEntitiesPage.getSize())
                .totalElements(transactionEntitiesPage.getTotalElements())
                .totalPages(transactionEntitiesPage.getTotalPages())
                .last(transactionEntitiesPage.isLast())
                .build();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        bookResponsePage
                )
        );
    }
}
