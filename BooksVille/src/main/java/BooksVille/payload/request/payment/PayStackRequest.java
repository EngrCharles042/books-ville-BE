package BooksVille.payload.request.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayStackRequest {
    private String message;
    private String redirecturl;
    private String reference;
    private String status;
    private String trans;
    private String transaction;
    private String trxref;
}
