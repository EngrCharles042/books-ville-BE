package BooksVille.payload.request;

import BooksVille.entities.enums.TransactStatus;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private BigDecimal amount;

    private TransactStatus status;

    private String referenceId;

    private Long bookEntityId;
}
