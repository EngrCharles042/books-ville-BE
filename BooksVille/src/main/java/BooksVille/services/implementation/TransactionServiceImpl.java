package BooksVille.services.implementation;

import BooksVille.entities.model.BookEntity;
import BooksVille.entities.model.TransactionEntity;
import BooksVille.entities.model.UserEntity;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.payload.request.TransactionRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.repositories.BookEntityRepository;
import BooksVille.repositories.TransactionEntityRepository;
import BooksVille.repositories.UserEntityRepository;
import BooksVille.services.TransactionService;
import BooksVille.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserEntityRepository userEntityRepository;
    private final ModelMapper modelMapper;
    private final TransactionEntityRepository transactionEntityRepository;
    private final HelperClass helperClass;
    private final HttpServletRequest httpServletRequest;
    private final JWTGenerator jwtGenerator;
    private final BookEntityRepository bookEntityRepository;

    @Override
    public ResponseEntity<ApiResponse<String>> bookPayment(TransactionRequest transactionRequest) {
        String email = jwtGenerator.getEmailFromJWT(helperClass.getTokenFromHttpRequest(httpServletRequest));

        UserEntity userEntity = userEntityRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new ApplicationException("User not found")
                );

        BookEntity bookEntity = bookEntityRepository
                .findById(transactionRequest.getBookEntityId())
                .orElseThrow(
                        () -> new ApplicationException("Book not found")
                );

        TransactionEntity transactionEntity = modelMapper.map(transactionRequest, TransactionEntity.class);

        transactionEntity.setUserEntity(userEntity);
        transactionEntity.setBookEntity(bookEntity);

        transactionEntityRepository.save(transactionEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "success"
                        )
                );
    }
}
