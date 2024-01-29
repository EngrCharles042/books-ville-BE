package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.TransactionRequest;
import BooksVille.payload.request.payment.PayStackRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<String>> payment(@Valid @RequestBody TransactionRequest transactionRequest) {
        return transactionService.bookPayment(transactionRequest);
    }

    @PostMapping("/webhook/url")
    public ResponseEntity<String> paystackWebhook() {
        System.out.println("webhooks called");

        return ResponseEntity.ok("Success");
    }

    @PostMapping("/paystack")
    public ResponseEntity<String> pay(@RequestBody PayStackRequest payStackRequest) {
        System.out.println(payStackRequest.getStatus());

        return ResponseEntity.ok(
                "Success"
        );
    }
}