package BooksVille.services;

import BooksVille.payload.request.payment.FlutterWaveRequest;
import BooksVille.payload.request.payment.PayStackRequest;
import BooksVille.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ApiResponse<String>> PayStackPayment(PayStackRequest payStackRequest, Long bookId);
    ResponseEntity<ApiResponse<String>> FlutterPayment(FlutterWaveRequest flutterWaveRequest, Long bookId);
}
