package booksville.infrastructure.controllers;

import booksville.payload.request.payment.FlutterWaveRequest;
import booksville.payload.request.payment.PayStackRequest;
import booksville.payload.response.ApiResponse;
import booksville.services.TransactionService;
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