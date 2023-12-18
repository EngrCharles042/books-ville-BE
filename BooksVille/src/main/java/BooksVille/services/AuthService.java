package BooksVille.services;

import BooksVille.payload.request.authRequest.ForgotPasswordResetRequest;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.authResponse.UserSignUpResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(UserSignUpRequest userSignUpRequest);
    ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(UserSignUpRequest userSignUpRequest);

    ResponseEntity<ApiResponse<String>> adminForgotPassword(String email);

    ResponseEntity<ApiResponse<String>> adminResetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest);
}
