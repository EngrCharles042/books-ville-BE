package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.authRequest.ForgotPasswordRequest;
import BooksVille.payload.request.authRequest.ForgotPasswordResetRequest;
import BooksVille.payload.request.authRequest.LoginRequest;
import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.authResponse.JwtAuthResponse;
import BooksVille.payload.response.authResponse.UserSignUpResponse;
import BooksVille.services.AuthService;
import BooksVille.utils.HelperClass;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JWTGenerator jwtGenerator;
    private final HelperClass helperClass;

    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return authService.registerUser(userSignUpRequest);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return authService.registerAdmin(userSignUpRequest);
    }

    @PostMapping("/admin-forgot-password")
    public ResponseEntity<ApiResponse<String>> adminForgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService.forgotPassword(forgotPasswordRequest.getEmail());
    }

    @PostMapping("/user-forgot-password")
    public ResponseEntity<ApiResponse<String>> userForgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authService.forgotPassword(forgotPasswordRequest.getEmail());
    }

    @PostMapping(value = "/admin-reset-forgot-password")
    public ResponseEntity<ApiResponse<String>> adminResetForgotPassword(@Valid @RequestBody ForgotPasswordResetRequest forgotPasswordResetRequest) {
        return authService.resetForgotPassword(forgotPasswordResetRequest);
    }

    @PostMapping(value = "/user-reset-forgot-password")
    public ResponseEntity<ApiResponse<String>> userResetForgotPassword(@Valid @RequestBody ForgotPasswordResetRequest forgotPasswordResetRequest) {
        return authService.resetForgotPassword(forgotPasswordResetRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/logout")
    private ResponseEntity<ApiResponse<String>> logout(){
        authService.logout();
        return ResponseEntity.ok(new ApiResponse<>("Logout Successfully"));
    }

    @GetMapping(value = "/verify-email", produces = MediaType.TEXT_HTML_VALUE)
    public String verifyToken(@RequestParam("token") String token) {
        ResponseEntity<ApiResponse<String>> response = authService.verifyToken(token);

        String email = jwtGenerator.getEmailFromJWT(token);
        String action = "Books Ville | Email Verification";
        String serviceProvider = "Books Ville Customer Portal Service";
        String description = Objects.requireNonNull(response.getBody()).getResponseMessage();

        String htmlResponse = helperClass.emailVerification(email, action, "Go to Login Page", serviceProvider, description);

        String state = Objects.requireNonNull(response.getBody()).getResponseData();

        if (state.equals("valid")) {
            return htmlResponse;
        }
        return "invalid";
    }
}
