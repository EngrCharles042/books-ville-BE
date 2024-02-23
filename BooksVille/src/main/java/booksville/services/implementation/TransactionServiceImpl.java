package booksville.services.implementation;

import booksville.entities.model.BookEntity;
import booksville.entities.model.TransactionEntity;
import booksville.entities.model.UserEntity;
import booksville.infrastructure.exceptions.ApplicationException;
import booksville.infrastructure.security.JWTGenerator;
import booksville.payload.request.payment.FlutterWaveRequest;
import booksville.payload.request.payment.PayStackRequest;
import booksville.payload.response.ApiResponse;
import booksville.repositories.BookRepository;
import booksville.repositories.CartRepository;
import booksville.repositories.TransactionEntityRepository;
import booksville.repositories.UserEntityRepository;
import booksville.services.TransactionService;
import booksville.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserEntityRepository userEntityRepository;
    private final TransactionEntityRepository transactionEntityRepository;
    private final HelperClass helperClass;
    private final HttpServletRequest httpServletRequest;
    private final JWTGenerator jwtGenerator;
    private final BookRepository bookEntityRepository;
    private final CartRepository cartRepository;

    @Override
    public ResponseEntity<ApiResponse<String>> PayStackPayment(PayStackRequest payStackRequest, Long bookId) {
        UserEntity userEntity = helperClass.getUserEntity();

        BookEntity bookEntity = bookEntityRepository
                .findById(bookId)
                .orElseThrow(
                        () -> new ApplicationException("Book not found")
                );

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(bookEntity.getPrice())
                .status(payStackRequest.getStatus())
                .referenceId(payStackRequest.getTrxref())
                .bookEntity(bookEntity)
                .userEntity(userEntity)
                .build();

        TransactionEntity savedTransaction = transactionEntityRepository.save(transactionEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "success",
                                savedTransaction.getStatus()
                        )
                );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> FlutterPayment(FlutterWaveRequest flutterWaveRequest, Long bookId) {
        UserEntity userEntity = helperClass.getUserEntity();

        BookEntity bookEntity = bookEntityRepository
                .findById(bookId)
                .orElseThrow(
                        () -> new ApplicationException("Book not found")
                );

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(bookEntity.getPrice())
                .status(flutterWaveRequest.getStatus())
                .referenceId(flutterWaveRequest.getTx_ref())
                .bookEntity(bookEntity)
                .userEntity(userEntity)
                .build();

        TransactionEntity savedTransaction = transactionEntityRepository.save(transactionEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "success",
                                savedTransaction.getStatus()
                        )
                );
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> PayStackPaymentCart(PayStackRequest payStackRequest) {
        UserEntity userEntity = helperClass.getUserEntity();

        payStackRequest.getBooks().forEach(
                        bookEntityResponse -> transactionEntityRepository.save(
                                TransactionEntity.builder()
                                        .amount(bookEntityResponse.getPrice())
                                        .status(payStackRequest.getStatus() + "_" + bookEntityResponse.getId())
                                        .referenceId(payStackRequest.getTrxref())
                                        .bookEntity(bookEntityRepository.findById(bookEntityResponse.getId()).get())
                                        .userEntity(userEntity)
                                        .build()
                        )
                );

        cartRepository.deleteAllByUserEntity(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "success",
                                "success"
                        )
                );
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> FlutterPaymentCart(FlutterWaveRequest flutterWaveRequest) {
        UserEntity userEntity = helperClass.getUserEntity();

        flutterWaveRequest.getBooks().forEach(
                bookEntityResponse -> transactionEntityRepository.save(
                        TransactionEntity.builder()
                                .amount(bookEntityResponse.getPrice())
                                .status(flutterWaveRequest.getStatus())
                                .referenceId(flutterWaveRequest.getTx_ref() + "_" + bookEntityResponse.getId())
                                .bookEntity(bookEntityRepository.findById(bookEntityResponse.getId()).get())
                                .userEntity(userEntity)
                                .build()
                )
        );

        cartRepository.deleteAllByUserEntity(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "success",
                                "completed"
                        )
                );
    }
}
