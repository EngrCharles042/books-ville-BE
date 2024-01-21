package BooksVille.services;

import BooksVille.payload.request.TransactionRequest;
import BooksVille.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ApiResponse<String>> bookPayment(TransactionRequest transactionRequest);
}
