package BooksVille.services;

import BooksVille.payload.request.authRequest.ForgotPasswordResetRequest;
import BooksVille.payload.request.authRequest.LoginRequest;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.authResponse.JwtAuthResponse;
import BooksVille.payload.response.authResponse.UserSignUpResponse;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(UserSignUpRequest userSignUpRequest);
    ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(UserSignUpRequest userSignUpRequest);
    ResponseEntity<ApiResponse<String>> forgotPassword(String email);
    ResponseEntity<ApiResponse<String>> resetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest);
    ResponseEntity<ApiResponse<JwtAuthResponse>> login(LoginRequest loginRequest);
    ResponseEntity<ApiResponse<JwtAuthResponse>> adminLogin(LoginRequest loginRequest);
    void logout();
    ResponseEntity<ApiResponse<String>> verifyToken(String receivedToken);
}
