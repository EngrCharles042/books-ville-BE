package BooksVille.services.implementation;

import BooksVille.entities.enums.Genre;
import BooksVille.entities.model.BookEntity;
import BooksVille.entities.model.UserEntity;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.payload.request.BookFilterRequest;
import BooksVille.payload.request.UserEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.repositories.BookRepository;
import BooksVille.repositories.UserEntityRepository;
import BooksVille.services.UserService;
import BooksVille.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final UserEntityRepository userEntityRepository;
    private final HelperClass helperClass;
    private final HttpServletRequest request;
    private final JWTGenerator jwtGenerator;

    @Override
    public UserEntity getUserEntity() {
        String token = helperClass.getTokenFromHttpRequest(request);

        String email = jwtGenerator.getEmailFromJWT(token);

        return  userEntityRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User does not exist with email " + email));
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

    @Override
    public ResponseEntity<ApiResponse<BookResponsePage>> filterBooks(int pageNo, int pageSize, String sortBy, String sortDir, BookFilterRequest bookFilterRequest) {
        // Sort condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BookEntity> filteredBooksPage = bookRepository.findBookEntitiesByGenreContainingAndRating(bookFilterRequest.getGenre(), bookFilterRequest.getRating(), pageable);

        List<BookEntity> bookEntities = filteredBooksPage.getContent();

        List<BookEntityResponse> bookEntityResponses = bookEntities.stream()
                .map(bookEntityResponse -> modelMapper.map(bookEntityResponse, BookEntityResponse.class))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Success",
                        BookResponsePage.builder()
                                .content(bookEntityResponses)
                                .pageNo(filteredBooksPage.getNumber())
                                .pageSize(filteredBooksPage.getSize())
                                .totalElements(filteredBooksPage.getTotalElements())
                                .totalPages(filteredBooksPage.getTotalPages())
                                .last(filteredBooksPage.isLast())
                                .build()
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> userInfoUpdate(UserEntityRequest userEntityRequest) {
        UserEntity userEntity = getUserEntity();

        userEntity.setFirstName(userEntityRequest.getFirstName());
        userEntity.setLastName(userEntityRequest.getLastName());
        userEntity.setPhoneNumber(userEntityRequest.getPhoneNumber());

        userEntityRepository.save(userEntity);

        return ResponseEntity.ok(new ApiResponse<>("update successful"));
    }
}
