package BooksVille.infrastructure.controllers;

import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
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
