package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.authRequest.ForgotPasswordRequest;
import BooksVille.payload.request.authRequest.ForgotPasswordResetRequest;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.authResponse.UserSignUpResponse;
import BooksVille.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return authService.registerUser(userSignUpRequest);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return authService.registerAdmin(userSignUpRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> adminForgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService.forgotPassword(forgotPasswordRequest.getEmail());
    }

    @PostMapping(value = "/reset-forgot-password")
    public ResponseEntity<ApiResponse<String>> adminResetForgotPassword(@Valid @RequestBody ForgotPasswordResetRequest forgotPasswordResetRequest) {

        return authService.resetForgotPassword(forgotPasswordResetRequest);
    }
}
