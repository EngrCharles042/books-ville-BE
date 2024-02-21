package booksville.services;

import booksville.payload.request.payment.FlutterWaveRequest;
import booksville.payload.request.payment.PayStackRequest;
import booksville.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ApiResponse<String>> PayStackPayment(PayStackRequest payStackRequest, Long bookId);
    ResponseEntity<ApiResponse<String>> FlutterPayment(FlutterWaveRequest flutterWaveRequest, Long bookId);
    ResponseEntity<ApiResponse<String>> PayStackPaymentCart(PayStackRequest payStackRequest);
    ResponseEntity<ApiResponse<String>> FlutterPaymentCart(FlutterWaveRequest flutterWaveRequest);
}
