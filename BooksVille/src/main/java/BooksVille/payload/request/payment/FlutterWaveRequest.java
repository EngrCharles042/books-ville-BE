package BooksVille.payload.request.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlutterWaveRequest {
    private String amount;
    private String charge_response_code;
    private String charge_response_message;
    private String charged_amount;
    private String created_at;
    private String currency;
    private PaidUser customer;
    private String flw_ref;
    private String redirectstatus;
    private String status;
    private String transaction_id;
    private String tx_ref;
}
