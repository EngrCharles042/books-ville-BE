package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.authRequest.LoginRequest;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.authResponse.JwtAuthResponse;
import BooksVille.payload.response.authResponse.UserSignUpResponse;
import BooksVille.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
    @GetMapping("/logout")
    private ResponseEntity<ApiResponse<String>> logout(){
        authService.logout();
        return ResponseEntity.ok(new ApiResponse<>("Logout Successfully"));
    }
}
