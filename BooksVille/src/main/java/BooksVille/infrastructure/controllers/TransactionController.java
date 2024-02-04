package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.TransactionRequest;
import BooksVille.payload.request.payment.FlutterWaveRequest;
import BooksVille.payload.request.payment.PayStackRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/paystack/{bookId}")
    public ResponseEntity<ApiResponse<String>> paystack(@RequestBody PayStackRequest payStackRequest,
                                           @PathVariable Long bookId) {
        return transactionService.PayStackPayment(payStackRequest, bookId);
    }

    @PostMapping("/flutter/{bookId}")
    public ResponseEntity<ApiResponse<String>> flutter(@RequestBody FlutterWaveRequest flutterWaveRequest,
                                          @PathVariable Long bookId) {
        return transactionService.FlutterPayment(flutterWaveRequest, bookId);
    }
}