package booksville.services;

import booksville.payload.request.authRequest.ForgotPasswordResetRequest;
import booksville.payload.request.authRequest.LoginRequest;
import booksville.payload.request.authRequest.UserSignUpRequest;
import booksville.payload.response.ApiResponse;
import booksville.payload.response.authResponse.JwtAuthResponse;
import booksville.payload.response.authResponse.UserSignUpResponse;
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
